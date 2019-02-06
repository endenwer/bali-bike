(ns bali-bike.ui.screens.new-booking-dates
  (:require [bali-bike.ui.components.dates-picker :as dates-picker]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [new-booking (rf/subscribe [:new-booking])]
    [dates-picker/main {:dates-range (:dates-range @new-booking)
                        :on-save #(rf/dispatch [:set-new-booking-dates-range %1 %2])}]))
