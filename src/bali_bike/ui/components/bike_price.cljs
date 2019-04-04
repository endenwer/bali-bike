(ns bali-bike.ui.components.bike-price
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [bali-bike.utils :as utils]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.colors :as colors]))

(defn main
  [{:keys [bike bold?]}]
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    (let [dates-diff (utils/get-dates-diff @dates-range)
          units (if (> (:months dates-diff) 0) "month" "day")
          price (if (> (:months dates-diff) 0) (:monthly-price bike) (:daily-price bike))]
      [view {:style {:flex-direction "row" :font-weight "600"}}
       (if bold?
         [:<>
          [text {:style {:font-weight "bold"}} (str "Rp " (utils/format-number price))]
          [text {:style {:color colors/silver :text-transform "uppercase"}} (str " /" units)]]
         [:<>
          [text (str "Rp " (utils/format-number price))]
          [text  (str " per " units)]])])))
