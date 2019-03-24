(ns bali-bike.ui.components.list-filter
  (:require [bali-bike.rn :refer [view search-bar list-item]]
            [bali-bike.utils :as utils]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main
  [data on-save]
  (r/with-let [value (r/atom "")]
    (let [filtered-data (utils/filter-map-by-value data @value)]
      [view {:style {:flex-direction "column" :align-items "stretch"}}
       [search-bar {:on-change-text #(reset! value %)
                    :value nil
                    :light-theme true
                    :container-style {:background-color colors/clouds
                                      :border-top-width 0}}]
       (for [[id title] filtered-data]
         ^{:key id} [list-item {:title title
                                :hide-chevron true
                                :on-press #(on-save id)}])])))
