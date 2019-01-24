(ns bali-bike.events.booking
  (:require [bali-bike.edb :as edb]
            [bali-bike.api :as api]
            [promesa.core :as p]
            [re-frame.core :as rf]))

;; effects

(defn create-booking
  [params]
  (.log js/console (clj->js params)))

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
  (let [bike-id (:id (edb/get-named-item db :bikes :current))
        dates-range (:dates-range db)]
    {:db (assoc db :booking-submiting? true)
     :booking/create {:bike-id bike-id
                      :start-date (:start-date dates-range)
                      :end-date (:end-date dates-range)}}))

(defn on-bookings-loaded-event
  [db [_ {:keys [data]}]]
  (edb/insert-collection db :bookings :list (:bookings data) {:loading? false}))

(defn load-bookings-event
  [{:keys [db]} [_ _]]
  {:db (edb/insert-meta db :bookings :list {:loading? true})
   :api/send-graphql {:query (str "{bookings {id startDate endDate status "
                                  "bike {id modelId photos}}}")
                      :callback-event :on-bookings-loaded}})

(defn navigate-to-booking-event
  [{:keys [db]} [_ booking-id]]
  {:db (edb/insert-named-item db :bookings :current {:id booking-id})
   :navigation/navigate-to :booking})

(defn update-delivery-region
  [{:keys [db]} [_ region]]
  {:dispatch [:update-delivery-location {:region region :loading? true}]
   :booking/load-delivery-address {:lat (:latitude region)
                                   :lng (:longitude region)}})

(defn update-delivery-location
  [db [_ data]]
  (let [delivery-location (:delivery-location db)
        updated-delivery-location (merge delivery-location data)]
    (assoc db :delivery-location updated-delivery-location)))

(defn set-delivery-location
  [{:keys [db]} [_ _]]
  (let [delivery-location (:delivery-location db)]
    {:db (assoc-in db [:new-booking :delivery-location] delivery-location)
     :navigation/navigate-to :new-booking}))

(defn navigate-to-new-booking-event
  [{:keys [db]} [_ _]]
  {:db (assoc-in db [:new-booking :dates-range] (:dates-range db))
   :navigation/navigate-to :new-booking})
