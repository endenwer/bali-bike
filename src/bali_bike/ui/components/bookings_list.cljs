(ns bali-bike.ui.components.bookings-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.booking-card :as booking-card]
            [bali-bike.colors :as colors]
            [bali-bike.rn :refer [scroll-view activity-indicator view]]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bookings (rf/subscribe [:bookings])]
    (let [bookings-meta (meta @bookings)]
      (if (:loading? bookings-meta)
        [view {:style {:flex 1 :align-items "center" :justify-content "center"}}
         [activity-indicator {:size "large" :color colors/turquoise}]]
        [scroll-view {:style {:flex 1 :padding-top 20} :showsVerticalScrollIndicator false}
         (for [booking-data @bookings]
           ^{:key (:id booking-data)} [booking-card/main booking-data])]))))
