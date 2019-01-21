(ns bali-bike.ui.screens.booking
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.rn :refer [view]]))

(defn main []
  (r/with-let [booking-data (rf/subscribe [:current-booking])]
    [view {:style {:flex 1}}
     [text (:id @booking-data)]]))
