(ns bali-bike.ui.components.bikes-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.bike :as bike]
            [bali-bike.colors :as colors]
            [bali-bike.rn :refer [scroll-view activity-indicator view]]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bikes (rf/subscribe [:bikes])]
    (let [bikes-meta (meta @bikes)]
      (if (:loading? bikes-meta)
        [view {:style {:flex 1 :align-items "center" :justify-content "center"}}
         [activity-indicator {:size "large" :color colors/turquoise}]]
        [scroll-view {:style {:flex 1 :padding-top 20} :showsVerticalScrollIndicator false}
         (for [bike-data @bikes]
           ^{:key (:id bike-data)} [bike/main bike-data])]))))
