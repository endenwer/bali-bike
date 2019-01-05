(ns bali-bike.ui.components.filter-buttons
  (:require [bali-bike.rn :refer [text view touchable-highlight]]
            [bali-bike.colors :as colors]
            [bali-bike.constants :as constants]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def moment (js/require "moment"))

(defn button-styles [is-active]
  {:background-color (if is-active colors/turquoise colors/white)
   :margin-horizontal 5
   :margin-bottom 10
   :padding-horizontal 10
   :border-width (if is-active 0 1)
   :height 30
   :align-items "center"
   :justify-content "center"
   :border-radius 20
   :border-color colors/silver})

(defn button-text-styles [is-active]
  {:color (if is-active colors/white colors/midnight-blue)
   :font-weight (if is-active "bold" "normal")})

(defn- render-button
  [{:keys [title is-active filter-screen-name]}]
  [touchable-highlight
   {:on-press #(rf/dispatch [:navigate-to filter-screen-name]) :style (button-styles is-active)}
   [text {:style (button-text-styles is-active)} title]])


(defn area []
  (r/with-let [area-filter-id (rf/subscribe [:area-filter-id])]
    [render-button {:title (get constants/areas @area-filter-id "Any area")
                    :is-active @area-filter-id
                    :filter-screen-name :area-filter}]))

(defn dates []
  (r/with-let [dates-range (rf/subscribe [:dates-range])]
    (let [start-date (:start-date @dates-range)
          end-date (:end-date @dates-range)]
      [render-button {:title (if @dates-range
                               (str (.format (moment start-date) "MMM D")
                                    " - "
                                    (.format (moment end-date) "MMM D"))
                               "Any dates")
                      :is-active @dates-range
                      :filter-screen-name :dates-filter}])))

(defn model []
  [render-button {:title "Any model" :filter-screen-name :area-filter}])

(defn filters []
  [render-button {:title "Filters" :filter-screen-name :area-filter}])
