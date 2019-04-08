(ns bali-bike.ui.components.filter-buttons
  (:require [bali-bike.rn :refer [view touchable-highlight]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.colors :as colors]
            [bali-bike.constants :as constants]
            [bali-bike.utils :as utils]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def moment (js/require "moment"))

(defn button-styles [is-active]
  {:background-color (if is-active colors/emerald colors/white)
   :margin-right 5
   :margin-bottom 10
   :padding-horizontal 10
   :border-width (if is-active 0 1)
   :height 30
   :align-items "center"
   :justify-content "center"
   :border-radius 5
   :border-color colors/clouds})

(defn button-text-styles [is-active]
  {:color (if is-active colors/white colors/midnight-blue)
   :font-weight (if is-active "bold" "normal")})

(defn- render-button
  [{:keys [title is-active filter-screen-name]}]
  [touchable-highlight
   {:on-press #(rf/dispatch [:navigate-to filter-screen-name]) :style (button-styles is-active)
    :underlay-color "transparent"}
   [text {:style (button-text-styles is-active)} title]])


(defn area []
  (r/with-let [area-filter-id (rf/subscribe [:area-filter-id])
               constants (rf/subscribe [:constants])]
    [render-button {:title (get-in @constants [:areas @area-filter-id] "Area")
                    :is-active @area-filter-id
                    :filter-screen-name :area-filter}]))

(defn dates []
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    (let [start-date (:start-date @dates-range)
          end-date (:end-date @dates-range)]
      [render-button {:title (if @dates-range
                               (utils/get-short-dates-range-string
                                (:start-date @dates-range)
                                (:end-date @dates-range))
                               "Dates")
                      :is-active @dates-range
                      :filter-screen-name :dates-filter}])))
