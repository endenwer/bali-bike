(ns bali-bike.ui.components.search-header
  (:require [bali-bike.rn :refer [safe-area-view touchable-highlight]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.filter-buttons :as filter-buttons]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]))

(defn main []
  [safe-area-view {:style {:flex-direction "row"
                           :flex-wrap "wrap"
                           :align-self "stretch"
                           :align-items "center"
                           :border-bottom-width 1
                           :border-color colors/silver}}
   [filter-buttons/area]
   [filter-buttons/dates]
   [filter-buttons/model]
   [filter-buttons/filters]])
