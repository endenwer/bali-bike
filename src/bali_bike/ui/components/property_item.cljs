(ns bali-bike.ui.components.property-item
  (:require
   [bali-bike.rn :refer [text view]]
   [bali-bike.colors :as colors]))

(defn main
  [property-name value]
  [view {:style {:padding-vertical 10
                 :border-bottom-width 1
                 :border-color colors/clouds
                 :flex-direction "row"
                 :justify-content "space-between"}}
   [text {:style {:font-size 20}} property-name]
   [text {:style {:font-size 20}} value]])
