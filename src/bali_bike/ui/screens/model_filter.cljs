(ns bali-bike.ui.screens.model-filter
  (:require [bali-bike.rn :refer [view search-bar list-item]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [filtered-models (rf/subscribe [:filtered-models])
               model-search-bar-text (rf/subscribe [:model-search-bar-text])]
    [view {:style {:flex-direction "column" :align-items "stretch"}}
     [search-bar {:on-change-text #(rf/dispatch [:change-model-search-bar-text %])
                  :value @model-search-bar-text
                  :light-theme true
                  :container-style {:background-color colors/clouds
                                    :border-top-width 0}}]
     (for [[id title] @filtered-models]
       ^{:key id} [list-item {:title title
                              :hide-chevron true
                              :on-press #(rf/dispatch [:set-model-filter-id id])}])]))
