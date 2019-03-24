(ns bali-bike.ui.components.booking-card
  (:require [reagent.core :as r]
            [bali-bike.utils :as utils]
            [bali-bike.rn :refer [view image touchable-highlight]]
            [bali-bike.ui.components.common :refer [text h3]]
            [re-frame.core :as rf]
            [bali-bike.colors :as colors]
            [bali-bike.constants :as constants]
            [bali-bike.ui.components.bike-title :as bike-title]))

(def moment (js/require "moment"))

(defn render-status
  [status]
  [text {:style {:color (get constants/status-colors status)
                 :font-weight "bold"}}
   (get constants/statuses status)])

(defn render-bike-photo [photo-url]
  [view {:style {:align-items "center" :width 100 :height 100}}
   [image {:style {:flex 1 :background-color "skyblue" :align-self "stretch"}
           :source {:uri photo-url}}]])

(defn render-dates
  [start-date end-date]
  [text {:style {:color colors/silver}} (utils/get-short-dates-range-string start-date end-date)])

(defn main [booking-data]
  (r/with-let [get-bike (:bike booking-data)]
    (let [bike-data (get-bike)]
      [touchable-highlight {:on-press #(rf/dispatch [:navigate-to-booking (:id booking-data)])}
       [view {:flex-direction "row" :margin-bottom 20}
        [render-bike-photo (first (:photos bike-data))]
        [view {:margin-left 10}
         [render-status (:status booking-data)]
         [bike-title/main bike-data]
         [render-dates (:start-date booking-data) (:end-date booking-data)]]]])))
