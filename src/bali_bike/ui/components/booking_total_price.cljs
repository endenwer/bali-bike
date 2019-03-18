(ns bali-bike.ui.components.booking-total-price
  (:require [bali-bike.ui.components.property-item :as property-item]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [h3]]
            [bali-bike.utils :as utils]
            [bali-bike.colors :as colors]))

(defn render-price
  [title months-count price]
  [property-item/main title (str "Rp " (utils/format-number price) " x " months-count)])

(defn main
  [{:keys [start-date end-date monthly-price daily-price] :as params}]
  (let [dates-diff (utils/get-dates-diff {:start-date start-date :end-date end-date})
        calculated-daily-price (if (> (:months dates-diff) 0)
                                 (utils/round-to-thousands (/ monthly-price 30))
                                 daily-price)]
    [view {:style {:padding-vertical 10}}
     [view {:style {:flex-direction "row"
                    :justify-content "space-between"
                    :align-items "center"
                    :margin-bottom 5}}
      [h3 "Total payment"]]
     (when (> (:months dates-diff) 0) [render-price "Months" (:months dates-diff) monthly-price])
     [render-price "Days" (:days dates-diff) calculated-daily-price]
     [property-item/main "Total" (str "Rp " (utils/format-number
                                             (+ (* monthly-price (:months dates-diff))
                                                (* calculated-daily-price (:days dates-diff)))))]]))
