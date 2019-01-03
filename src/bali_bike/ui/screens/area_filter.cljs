(ns bali-bike.ui.screens.area-filter
  (:require [bali-bike.rn :refer [text view search-bar list list-item]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [filtered-areas (rf/subscribe [:filtered-areas])]
    [view {:style {:flex-direction "column" :align-items "stretch"}}
     [search-bar {:on-change-text #(rf/dispatch [:change-area-search-bar-text %])
                  :light-theme true
                  :container-style {:background-color colors/clouds
                                    :border-top-width 0}}]
     [list {:container-style {:margin-top 0}}
      (for [[id title] @filtered-areas]
        ^{:key id} [list-item {:title title
                               :hide-chevron true
                               :on-press #(rf/dispatch [:set-area-filter-id id])}])]]))
