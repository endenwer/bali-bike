(ns bali-bike.ui.screens.area-filter
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.ui.components.list-filter :as list-filter]))

(defn main []
  (r/with-let [constants (rf/subscribe [:constants])]
    [list-filter/main (:areas @constants) #(rf/dispatch [:set-area-filter-id %])]))
