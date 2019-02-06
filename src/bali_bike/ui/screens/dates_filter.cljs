(ns bali-bike.ui.screens.dates-filter
  (:require [bali-bike.ui.components.dates-picker :as dates-picker]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    [dates-picker/main {:dates-range @dates-range
                        :on-save #(rf/dispatch [:set-dates-range %1 %2])}]))
