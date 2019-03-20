(ns bali-bike.ui.components.dates-picker
  (:require [bali-bike.rn :refer [safe-area-view view]]
            [bali-bike.ui.components.common :refer [button text]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]))

(def CalendarPicker (.-default (js/require "react-native-calendar-picker")))
(def calendar-picker (r/adapt-react-class CalendarPicker))
(def moment (js/require "moment"))

(defn render-selected-date
  [date title]
  [text {:style {:font-size 20}}
   (if date (.format date "MMM D") title)])

(defn render-selected-dates
  [start-date end-date]
  [view {:style {:flex-direction "row"
                 :align-items "center"
                 :margin-top 40
                 :justify-content "center"}}
   [render-selected-date start-date "Start date"]
   [text {:style {:font-size 50
                  :margin-horizontal 30
                  :color colors/clouds}} "/"]
   [render-selected-date end-date "End date"]])

(defn main
  [{:keys [dates-range disabled-dates on-save]}]
  (r/with-let [saved-start-date (:start-date dates-range)
               saved-end-date (:end-date dates-range)
               start-date (r/atom (if saved-start-date (moment saved-start-date) nil))
               end-date (r/atom (if saved-end-date (moment saved-end-date) nil))
               on-date-change (fn [date type] (if (= type "END_DATE")
                                                (reset! end-date date)
                                                (do (reset! end-date nil)
                                                    (reset! start-date date))))]
    [view {:style {:flex 1
                   :flex-direction "column"}}
     [calendar-picker {:allow-range-selection true
                       :selected-day-color colors/emerald
                       :selected-day-text-color colors/white
                       :start-from-monday true
                       :min-date (js/Date.)
                       :selected-start-date @start-date
                       :selected-end-date @end-date
                       :on-date-change on-date-change
                       :disabled-dates disabled-dates}]
     [render-selected-dates @start-date @end-date]
     [safe-area-view {:style {:flex 1
                              :justify-content "flex-end"
                              :margin-bottom 20}}
      [button {:title "Save"
               :disabled (or (nil? @start-date) (nil? @end-date))
               :button-style {:background-color colors/emerald
                              :margin-horizontal 10}
               :on-press #(on-save (.toISOString @start-date) (.toISOString @end-date))}]]]))
