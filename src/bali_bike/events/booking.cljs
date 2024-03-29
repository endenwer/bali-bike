(ns bali-bike.events.booking
  (:require [bali-bike.edb :as edb]
            [bali-bike.api :as api]
            [promesa.core :as p]
            [re-frame.core :as rf]
            [clojure.string :as string]))

(defn booking-full-data-query
  [user-role]
  (let [base-query [:id :startDate :endDate :status
                    :monthlyPrice :dailyPrice
                    :weeklyPrice :totalPrice
                    :deliveryLocationLongitude
                    :deliveryLocationLongitudeDelta
                    :deliveryLocationLatitude
                    :deliveryLocationLatitudeDelta
                    :deliveryLocationAddress
                    :deliveryLocationComment]]
    (into [] (if (= "bike-owner" user-role)
               (concat base-query [:user [:uid :name :photoURL]
                                   :bike [:id :modelId :photos]])
               (concat base-query [:bike [:id :modelId :photos
                                          :owner [:uid :name :photoURL]]])))))

;; effects

(defn load-delivery-address
  [{:keys [lat lng]}]
  (->
   (api/get-address-from-coordinates lat lng)
   (p/then (fn [address]
             (rf/dispatch [:update-delivery-location {:address address :loading? false}])))
   (p/catch (fn [error] (.log js/console error)))))

;; events

(defn create-booking-event
  [{:keys [db]} [_ _]]
  (let [user (:current-user db)
        bike-id (:id (edb/get-named-item db :bikes :current))
        dates-range (:dates-range db)
        new-booking (:new-booking db)]
    {:db (assoc-in db [:new-booking :submiting?] true)
     :api/send-graphql {:mutation
                        [:createBooking {:bikeId bike-id
                                         :startDate
                                         (get-in new-booking [:dates-range :start-date])
                                         :endDate
                                         (get-in new-booking [:dates-range :end-date])
                                         :deliveryLocationLatitude
                                         (str
                                          (get-in
                                           new-booking
                                           [:delivery-location :region :latitude]))
                                         :deliveryLocationLatitudeDelta
                                         (str
                                          (get-in
                                           new-booking
                                           [:delivery-location :region :latitude-delta]))
                                         :deliveryLocationLongitudeDelta
                                         (str
                                          (get-in
                                           new-booking
                                           [:delivery-location :region :longitude-delta]))
                                         :deliveryLocationLongitude
                                         (str
                                          (get-in
                                           new-booking
                                           [:delivery-location :region :longitude]))
                                         :deliveryLocationAddress
                                         (get-in new-booking [:delivery-location :address])}
                         (booking-full-data-query (:role user))]
                        :callback-event :on-booking-created}}))

(defn on-bookings-loaded-event
  [db [_ {:keys [data]}]]
  (let [bookings (or (:bookings data) (:bike-owner-bookings data))]
    (edb/insert-collection db :bookings :list bookings {:loading? false :reloading? false})))

(defn load-bookings-event
  [{:keys [db]} [_ reloading?]]
  (let [user (:current-user db)
        query-name (if (= "bike-owner" (:role user)) :bikeOwnerBookings :bookings)]
    {:db (edb/insert-meta db :bookings :list (if reloading? {:reloading? true} {:loading? true}))
     :api/send-graphql {:query [query-name [:id :startDate :endDate :status
                                            :bike [:id :modelId :photos]]]
                        :callback-event :on-bookings-loaded}}))

(defn on-booking-loaded-event
  [db [_ {:keys [data]}]]
  (let [user (:current-user db)
        booking (:booking data)
        contact-user (if (= "bike-owner" (:role user))
                       (:user booking)
                       (get-in booking [:bike :owner]))]
    (edb/insert-named-item
     db :bookings :current (assoc booking :contact-user contact-user) {:loading? false})))

(defn navigate-to-booking-event
  [{:keys [db]} [_ booking-id]]
  (let [user (:current-user db)]
    {:db (edb/insert-named-item db :bookings :current {:id booking-id} {:loading? true})
     :api/send-graphql {:query [:booking {:id booking-id} (booking-full-data-query (:role user))]
                        :callback-event :on-booking-loaded}
     :navigation/navigate-to :booking}))

(defn on-booking-created-event
  [{:keys [db]} [_ {:keys [data]}]]
  {:db (-> (on-booking-loaded-event db [nil {:data {:booking (:create-booking data)}}])
           (assoc-in [:new-booking :submiting?] false))
   :navigation/replace :booking
   :dispatch [:load-bookings]})

(defn update-delivery-region-event
  [{:keys [db]} [_ region]]
  {:dispatch [:update-delivery-location {:region region :loading? true}]
   :booking/load-delivery-address {:lat (:latitude region)
                                   :lng (:longitude region)}})

(defn update-delivery-location-event
  [db [_ data]]
  (let [delivery-location (:delivery-location db)
        updated-delivery-location (merge delivery-location data)]
    (assoc db :delivery-location updated-delivery-location)))

(defn set-delivery-location-event
  [{:keys [db]} [_ _]]
  (let [delivery-location (:delivery-location db)]
    {:db (assoc-in db [:new-booking :delivery-location] delivery-location)
     :navigation/navigate-to :new-booking}))

(defn navigate-to-new-booking-event
  [{:keys [db]} [_ _]]
  (let [dates-range (:dates-range db)
        new-booking-dates-range (get-in db [:new-booking :dates-range])]
    (if (or dates-range new-booking-dates-range)
      {:db (assoc-in db [:new-booking :dates-range] (or dates-range new-booking-dates-range))
       :navigation/navigate-to :new-booking}
      {:navigation/navigate-to :new-booking-dates})))

(defn set-new-booking-dates-range-event
  [{:keys [db]} [_ start-date end-date]]
  {:db (assoc-in db [:new-booking :dates-range] {:start-date start-date :end-date end-date})
   :navigation/replace :new-booking})

(defn cancel-booking-event
  [{:keys [db]} [_ ]]
  (let [current-booking (edb/get-named-item db :bookings :current)]
    {:db (edb/update-named-item db :bookings :current {:status "CANCELED"})
     :api/send-graphql {:mutation [:cancelBooking {:id (:id current-booking)} [:id]]}}))

(defn confirm-booking-event
  [{:keys [db]} [_ ]]
  (let [current-booking (edb/get-named-item db :bookings :current)]
    {:db (edb/update-named-item db :bookings :current {:status "CONFIRMED"})
     :api/send-graphql {:mutation [:confirmBooking {:id (:id current-booking)} [:id]]}}))
