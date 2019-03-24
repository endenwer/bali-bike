(ns bali-bike.ui.screens.model-filter
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.ui.components.list-filter :as list-filter]))

(defn main []
  (r/with-let [constants (rf/subscribe [:constants])]
    [list-filter/main (:models @constants) #(rf/dispatch [:set-model-filter-id %])]))
