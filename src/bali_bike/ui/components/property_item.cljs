(ns bali-bike.ui.components.property-item
  (:require
   [bali-bike.rn :refer [view]]
   [bali-bike.ui.components.common :refer [text]]
   [bali-bike.colors :as colors]))

(defn main
  [property-name value]
  [view {:style {:padding-bottom 10
                 :margin-bottom 10
                 :border-bottom-width 1
                 :border-color colors/clouds
                 :flex-direction "row"
                 :justify-content "space-between"}}
   [text property-name]
   [text value]])
