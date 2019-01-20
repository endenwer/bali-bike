(ns bali-bike.subs
  (:require [re-frame.core :as rf]
            [clojure.string :as string]
            [bali-bike.edb :as edb]
            [bali-bike.constants :as constants]))

(rf/reg-sub
 :bikes
 (fn [app-db _]
   (edb/get-collection app-db :bikes :list)))

(rf/reg-sub
 :area-filter-id
 (fn [app-db _]
   (:area-filter-id app-db)))

(rf/reg-sub
 :filtered-areas
 (fn [app-db _]
   (if-let [area-search-bar-text (:area-search-bar-text app-db)]
     (filter
      #(string/includes? (string/lower-case (second %)) (string/lower-case area-search-bar-text))
      constants/areas)
     constants/areas)))

(rf/reg-sub
 :dates-range
 (fn [app-db _]
   (:dates-range app-db)))

(rf/reg-sub
 :current-bike
 (fn [app-db _]
   (edb/get-named-item app-db :bikes :current)))

(rf/reg-sub
 :bookings
 (fn [app-db _]
   (edb/get-collection app-db :bookings :list)))
