(ns bali-bike.ui.components.bikes-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.bike :as bike]
            [bali-bike.rn :refer [view]]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bikes (rf/subscribe [:bikes])]
    [view {:style {:flex 1}}
     (for [bike-data @bikes]
       ^{:key (:id bike-data)} [bike/main bike-data])]))
