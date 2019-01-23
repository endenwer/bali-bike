(ns bali-bike.ui.screens.booking
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.colors :as colors]
            [bali-bike.utils :as utils]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.rn :refer [view scroll-view safe-area-view]]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.constants :as constants]))

(defn render-status
  [status]
  [view {:style {:height 50
                 :flex 1
                 :background-color (get constants/status-colors status)
                 :justify-content "center"
                 :align-items "center"}}
   [text {:style {:color colors/white
                  :font-weight "bold"}}
    (get constants/statuses status)]])

(defn render-dates
  [start-date end-date]
  [text {:style {:font-weight "bold"}}
   (utils/get-short-dates-range-string start-date end-date)])

(defn main []
  (r/with-let [booking-data (rf/subscribe [:current-booking])]
    (let [get-bike (:bike @booking-data)
          bike-data (get-bike)]
      [view {:style {:flex 1}}
       [scroll-view {:style {:flex 1}}
        [safe-area-view {:style {:flex 1}}
         [bike-photos-swiper/main bike-data]]
        [render-status (:status @booking-data)]
        [view {:style {:flex 1 :margin-horizontal 10 :margin-top 20}}
         [render-dates (:start-date @booking-data) (:end-date @booking-data)]
         [bike-title/main bike-data]]]
       [text (:id @booking-data)]])))
