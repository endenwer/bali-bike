(ns bali-bike.ui.components.bike-price
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [bali-bike.utils :as utils]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]))

(defn main
  [bike]
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    (let [dates-diff (utils/get-dates-diff @dates-range)
          units (if (> (:months dates-diff) 0) "month" "day")
          price (if (> (:months dates-diff) 0) (:monthly-price bike) (:daily-price bike))]
      [view {:style {:flex-direction "row" :font-weight "600"}}
       [text (str "Rp " (utils/format-number price))]
       [text (str " per " units)]])))
