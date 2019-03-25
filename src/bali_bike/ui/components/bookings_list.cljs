(ns bali-bike.ui.components.bookings-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.booking-card :as booking-card]
            [bali-bike.colors :as colors]
            [bali-bike.rn :refer [scroll-view activity-indicator view RefreshControl]]
            [bali-bike.ui.components.full-screen-loading :as full-screen-loading]
            [bali-bike.ui.components.empty-icon :as empty-icon]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bookings (rf/subscribe [:bookings])
               bookings-meta (rf/subscribe [:bookings-meta])]
    (if (:loading? @bookings-meta)
      [full-screen-loading/main]
      (if (= 0 (count @bookings))
        [empty-icon/main]
        [scroll-view {:style {:flex 1 :padding-top 20}
                      :showsVerticalScrollIndicator false
                      :refreshControl (r/as-element
                                       [:> RefreshControl
                                        {:refreshing (:reloading? @bookings-meta)
                                         :onRefresh #(rf/dispatch [:load-bookings true])}])}
         (for [booking-data @bookings]
           ^{:key (:id booking-data)} [booking-card/main booking-data])]))))
