(ns bali-bike.events
  (:require
   [re-frame.core :refer [reg-event-db after] :as rf]
   [clojure.spec.alpha :as s]
   [bali-bike.edb :as edb]
   [bali-bike.routing :as routing]))

(rf/reg-fx
 ::navigate-to
 (fn [view-id]
   (routing/navigate-to view-id)))

(rf/reg-fx
 ::navigate-back
 (fn []
   (routing/navigate-back)))

(rf/reg-event-fx
 :navigate-to
 (fn [_ [_ view-id]]
   {::navigate-to view-id}))

(rf/reg-event-fx
 :navigate-back
 (fn [_ _]
   {::navigate-back nil}))

(rf/reg-event-db
 :change-area-search-bar-text
 (fn [app-db [_ value]]
   (assoc app-db :area-search-bar-text value)))

(rf/reg-event-fx
 :set-area-filter-id
 (fn [{:keys [db]} [_ area-id]]
   {:db (-> db
            (assoc :area-filter-id area-id)
            (dissoc :area-search-bar-text))
    ::navigate-back nil}))

(rf/reg-event-fx
 :set-dates-range
 (fn [{:keys [db]} [_ start-date end-date]]
   {:db (assoc db :dates-range {:start-date start-date :end-date end-date})
    ::navigate-back nil}))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   edb/initial-app-db))
