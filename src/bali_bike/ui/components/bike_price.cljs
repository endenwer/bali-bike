(ns bali-bike.ui.components.bike-price
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [bali-bike.utils :as utils]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.colors :as colors]))

(defn get-units
  [{:keys [days months]}]
  (cond
    (> months 0) "month"
    (> (quot days 7) 0) "week"
    :else "day"))

(defn get-price
  [{:keys [daily-price weekly-price monthly-price]} {:keys [days months]}]
  (cond
    (> months 0) monthly-price
    (> (quot days 7) 0) weekly-price
    :else daily-price))

(defn main
  [{:keys [bike bold?]}]
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    (let [dates-diff (utils/get-dates-diff @dates-range)
          days (:days dates-diff)
          weeks (quot days 7)
          months (:months dates-diff)
          units (get-units dates-diff)
          price (get-price bike dates-diff)]
      [view {:style {:flex-direction "row" :font-weight "600"}}
       (if price
         (if bold?
           [:<>
            [text {:style {:font-weight "bold"}} (str "Rp " (utils/format-number price))]
            [text {:style {:color colors/silver :text-transform "uppercase"}} (str " /" units)]]
           [:<>
            [text (str "Rp " (utils/format-number price))]
            [text  (str " per " units)]])
         [text "Ask for price"])])))
