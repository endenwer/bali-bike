(ns bali-bike.ui.components.search-header
  (:require [bali-bike.rn :refer [safe-area-view touchable-highlight view icon]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.filter-buttons :as filter-buttons]
            [re-frame.core :as rf]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [bali-bike.constants :as constants]))

(defn render-model-filter []
  (r/with-let [model-filter-id (rf/subscribe [:model-filter-id])]
    (let [model (get constants/models @model-filter-id)]
      [touchable-highlight {:on-press #(rf/dispatch [:navigate-to :model-filter])}
       [view {:style {:flex-direction "row"
                      :align-self "stretch"
                      :justify-content "flex-start"
                      :align-items "center"
                      :border-radius 5
                      :margin-bottom 10
                      :height 40
                      :shadow-color colors/clouds
                      :shadow-offset {:width 0 :height 1}
                      :shadow-opacity 0.70
                      :shadow-radius 2
                      :elevation 10
                      :background-color colors/clouds}}
        [icon {:name "search"
               :color colors/silver
               :container-style {:padding-horizontal 10}}]
        (if model
          [text {:style {:font-weight "bold"}} model]
          [text {:style {:font-weight "bold" :color colors/silver}} "All bikes"])]])))

(defn main []
  [safe-area-view {:style {:margin-top 10
                           :flex-wrap "wrap"
                           :align-self "stretch"}}
   [render-model-filter]
   [view {:flex-direction "row"}
    [filter-buttons/area]
    [filter-buttons/dates]]])
