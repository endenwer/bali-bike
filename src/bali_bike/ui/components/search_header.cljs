(ns bali-bike.ui.components.search-header
  (:require [bali-bike.rn :refer [text safe-area-view touchable-highlight]]
            [bali-bike.ui.components.filter-buttons :as filter-buttons]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]))

(defn main []
  [safe-area-view {:style {:flex-direction "row"
                           :flex-wrap "wrap"
                           :padding-horizontal 10
                           :align-self "stretch"
                           :align-items "center"
                           :border-bottom-width 1
                           :border-color colors/silver
                           :background-color colors/clouds}}
   [filter-buttons/area]
   [filter-buttons/model]
   [filter-buttons/dates]
   [filter-buttons/filters]])
