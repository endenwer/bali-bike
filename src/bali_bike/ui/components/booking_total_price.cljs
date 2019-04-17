(ns bali-bike.ui.components.booking-total-price
  (:require [bali-bike.ui.components.property-item :as property-item]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.utils :as utils]
            [bali-bike.colors :as colors]))

(defn get-calculated-daily-price
  [days weeks months daily-price weekly-price monthly-price]
  (cond
    (> months 0) (utils/round-to-thousands (/ monthly-price 30))
    (> weeks 0) (utils/round-to-thousands (/ weekly-price 7))
    :else daily-price))

(defn get-total-price
  [days weeks months daily-price weekly-price monthly-price]
  (cond
    (> months 0) (+ (* monthly-price months)
                    (* (utils/round-to-thousands (/ monthly-price 30)) days))
    (> weeks 0) (+ (* weeks weekly-price)
                   (* (utils/round-to-thousands (/ weekly-price 7)) (- days (* weeks 7))))
    :else (* daily-price days)))

(defn render-price
  [title period-size price]
  [property-item/main title (str "Rp " (utils/format-number price) " x " period-size)])

(defn main
  [{:keys [start-date end-date monthly-price daily-price weekly-price] :as params}]
  (let [dates-diff (utils/get-dates-diff {:start-date start-date :end-date end-date})
        days (:days dates-diff)
        weeks (quot days 7)
        months (:months dates-diff)
        days-without-weeks (- days (* weeks 7))
        calculated-daily-price (get-calculated-daily-price
                                days weeks months
                                daily-price weekly-price monthly-price)
        total-price (get-total-price
                     days weeks months
                     daily-price weekly-price monthly-price)]
    [view {:style {:padding-vertical 10}}
     [view {:style {:flex-direction "row"
                    :justify-content "space-between"
                    :align-items "center"
                    :margin-bottom 5}}
      [text {:style {:font-weight "bold"}} "Total payment"]]
     (when (> months 0) [render-price "Months" months monthly-price])
     (when (> weeks 0) [render-price "Weeks" weeks weekly-price])
     (when (> days-without-weeks 0) [render-price "Days" days-without-weeks calculated-daily-price])
     [property-item/main "Total" (str "Rp " (utils/format-number total-price))]]))
