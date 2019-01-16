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
