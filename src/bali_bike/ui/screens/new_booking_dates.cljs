(ns bali-bike.ui.screens.new-booking-dates
  (:require [bali-bike.ui.components.dates-picker :as dates-picker]
            [bali-bike.utils :as utils]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [new-booking (rf/subscribe [:new-booking])
               current-bike (rf/subscribe [:current-bike])]
    (let [bookings ((:bookings @current-bike))]
      [dates-picker/main {:dates-range (:dates-range @new-booking)
                          :disabled-dates (reduce
                                           #(concat %1 (utils/dates-in-range %2)) [] bookings)
                          :on-save #(rf/dispatch [:set-new-booking-dates-range %1 %2])}])))
