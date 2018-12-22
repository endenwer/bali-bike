(ns bali-bike.ui.components.bikes-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.bike :as bike]
            [bali-bike.rn :refer [scroll-view]]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bikes (rf/subscribe [:bikes])]
    [scroll-view {:style {:flex 1}
                  :showsVerticalScrollIndicator false}
     (for [bike-data @bikes]
       ^{:key (:id bike-data)} [bike/main bike-data])]))
