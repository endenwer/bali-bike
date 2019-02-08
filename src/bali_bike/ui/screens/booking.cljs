(ns bali-bike.ui.screens.booking
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.colors :as colors]
            [bali-bike.utils :as utils]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.booking-total-price :as booking-total-price]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.rn :refer [view
                                  scroll-view
                                  safe-area-view
                                  touchable-highlight
                                  activity-indicator]]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.constants :as constants]
            [bali-bike.ui.screens.new-booking :as new-booking]
            [bali-bike.ui.components.avatar :as avatar]))

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

(defn render-owner
  [{:keys [booking-id owner]}]
  [view {:style {:flex-direction "row"
                 :margin-vertical 20
                 :padding-vertical 5
                 :align-items "center"
                 :border-bottom-width 1
                 :border-top-width 1
                 :border-color colors/clouds}}
   [avatar/main {:photo-url (:photo-url owner) :size "medium"}]
   [text {:style {:font-weight "bold" :flex 1 :margin-left 10}} (:name owner)]
   [touchable-highlight
    {:on-press #(rf/dispatch [:navigate-to-chat-from-booking booking-id])}
    [text {:style {:color colors/turquoise}} "SEND MESSAGE"]]])

(defn render-loading []
  [view {:style {:flex 1 :margin-top 30 :align-items "center" :justify-content "center"}}
   [activity-indicator {:size "large" :color colors/turquoise}]])

(defn main []
  (r/with-let [booking-data (rf/subscribe [:current-booking])]
    (let [booking-meta (meta @booking-data)
          get-bike (:bike @booking-data)
          bike-data (get-bike)
          owner (:owner bike-data)]
      [view {:style {:flex 1}}
       [scroll-view {:style {:flex 1} :showsVerticalScrollIndicator false}
        [safe-area-view {:style {:flex 1}}
         [bike-photos-swiper/main bike-data]]
        [render-status (:status @booking-data)]
        [view {:style {:flex 1 :margin-horizontal 10 :margin-top 20}}
         [render-dates (:start-date @booking-data) (:end-date @booking-data)]
         [bike-title/main bike-data]
         (if (:loading? booking-meta)
           [render-loading]
           [view {:style {:flex 1}}
            [render-owner {:booking-id (:id @booking-data) :owner owner}]
            [new-booking/render-property {:title "Delivery location"
                                          :on-press #(rf/dispatch [:navigate-to :booking-map])
                                          :on-press-text "SHOW MAP"
                                          :value (:delivery-location-address @booking-data)}]
            [booking-total-price/main {:monthly-price (:monthly-price @booking-data)
                                       :daily-price (:daily-price @booking-data)
                                       :start-date (:start-date @booking-data)
                                       :end-date (:end-date @booking-data)}]])]]])))
