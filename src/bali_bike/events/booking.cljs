(ns bali-bike.events.booking
  (:require [bali-bike.edb :as edb]))

;; effects

(defn create-booking
  [params]
  (.log js/console (clj->js params)))

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
