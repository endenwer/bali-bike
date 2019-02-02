(ns bali-bike.ui.screens.new-booking
  (:require [bali-bike.rn :refer [view safe-area-view scroll-view image touchable-highlight]]
            [bali-bike.ui.components.common :refer [text button h3]]
            [bali-bike.ui.components.property-item :as property-item]
            [reagent.core :as r]
            [bali-bike.colors :as colors]
            [bali-bike.rn :as rn]
            [bali-bike.constants :as constants]
            [bali-bike.ui.components.bike-rating :as bike-rating]
            [bali-bike.utils :as utils]
            [re-frame.core :as rf]))

(defn render-bottom
  [{:keys [on-submit submiting?]}]
  [safe-area-view {:style {:border-top-width 3
                           :border-color colors/clouds
                           :padding 10}}
   [button {:title "Book"
            :on-press on-submit
            :disabled submiting?
            :loading submiting?
            :background-color colors/alizarin
            :container-view-style {:margin 10}
            :text-style {:margin-horizontal 20 :font-weight "bold"}}]])

(defn render-bike-title
  [model-id]
  [view
   [h3 (get constants/models model-id)]])

(defn render-bike-photo [photo-url]
  [view {:style {:align-items "center" :width 100 :height 100}}
   [image {:style {:flex 1 :background-color "skyblue" :align-self "stretch"}
           :source {:uri photo-url}}]])

(defn render-bike-preview
  [bike-data]
  [view {:flex-direction "row" :margin-bottom 15}
   [render-bike-photo (first (:photos bike-data))]
   [view {:margin-left 10}
    [render-bike-title (:model-id bike-data)]
    [bike-rating/main bike-data]]])

(defn render-property
  [{:keys [title value on-change]}]
  [view {:style {:padding-vertical 15
                 :border-top-width 1
                 :border-color colors/clouds}}
   [view {:style {:flex-direction "row"
                  :justify-content "space-between"
                  :align-items "center"
                  :margin-bottom 10}}
    [h3 title]
    [touchable-highlight {:on-press on-change}
     [text {:style {:color colors/turquoise}} "CHANGE"]]]
   [text value]])

(defn render-total
  [{:keys [bike dates-range]}]
  (let [dates-diff (utils/get-dates-diff dates-range)
        daily-price (if (:months dates-diff)
                      (utils/round-to-thousands (/ (:monthly-price bike) 30))
                      (:daily-price bike))]
    [view {:style {:padding-vertical 15
                   :border-top-width 1
                   :border-color colors/clouds}}
     [view {:style {:flex-direction "row"
                    :justify-content "space-between"
                    :align-items "center"
                    :margin-bottom 10}}
      [h3 "Total payment"]]
     [property-item/main "Months" (str "Rp"
                                       (utils/format-number (:monthly-price bike))
                                       " x "
                                       (:months dates-diff))]
     [property-item/main "Days" (str "Rp"
                                     (utils/format-number daily-price) " x " (:days dates-diff))]
     [property-item/main "Total" (str "Rp" (utils/format-number
                                            (+ (* (:monthly-price bike) (:months dates-diff))
                                               (* daily-price (:days dates-diff)))))]]))

(defn main []
  (r/with-let [current-bike (rf/subscribe [:current-bike])
               new-booking (rf/subscribe [:new-booking])]
    (let [{:keys [delivery-location dates-range]} @new-booking]
      [view {:style {:flex 1}}
       [scroll-view {:style {:flex 1
                             :margin-horizontal 10
                             :margin-top 10}}
        [render-bike-preview @current-bike]
        [render-property {:title "Dates"
                          :on-change #(.log js/console "dates change")
                          :value (utils/get-short-dates-range-string
                                  (:start-date dates-range)
                                  (:end-date dates-range))}]
        [render-property {:title "Delivery location"
                          :on-change #(rf/dispatch [:navigate-to :new-booking-map])
                          :value (or
                                  (:address delivery-location)
                                  "Choose delivery location")}]
        [render-total {:bike @current-bike
                       :dates-range dates-range}]]
       [render-bottom
        {:on-submit #(rf/dispatch [:create-booking])
         :submiting? (:submiting? @new-booking)}]])))
