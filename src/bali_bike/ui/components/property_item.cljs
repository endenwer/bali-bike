(ns bali-bike.ui.components.property-item
  (:require
   [bali-bike.rn :refer [view icon]]
   [bali-bike.ui.components.common :refer [text]]
   [bali-bike.colors :as colors]))

(def styles
  {:padding-bottom 10
   :margin-bottom 10
   :border-bottom-width 1
   :border-color colors/clouds
   :flex-direction "row"
   :justify-content "space-between"})

(defn main
  [property-name value]
  [view {:style styles}
   [text property-name]
   [text value]])

(defn with-icon
  [icon-name value]
  [view {:style styles}
   [icon {:name icon-name :type "font-awesome"}]
   [text value]])
