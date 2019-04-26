(ns bali-bike.ui.screens.bike
  (:require [bali-bike.rn :as rn :refer [view scroll-view safe-area-view rating icon]]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.ui.components.property-item :as property-item]
            [bali-bike.ui.components.common :refer [button text h2]]
            [bali-bike.colors :as colors]
            [bali-bike.ui.components.bike-price :as bike-price]
            [bali-bike.ui.components.bike-rating :as bike-rating]
            [bali-bike.ui.components.reviews-list :as reviews-list]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn render-bottom [bike-data]
  [safe-area-view
   [view {:style {:flex-direction "row"
                  :border-top-width 3
                  :border-color colors/clouds
                  :padding 10
                  :align-items "center"
                  :justify-content "space-between"}}
    [view
     [bike-price/main {:bike bike-data :bold? true}]
     [bike-rating/main bike-data]]
    (if (:only-contacts bike-data)
      [button {:title "Whatsapp"
               :icon #(r/as-element [icon {:type "font-awesome"
                                           :name "whatsapp"
                                           :color colors/emerald}])
               :on-press #(.openURL rn/Linking (str "whatsapp://send?phone=" (:whatsapp bike-data)))
               :title-style {:font-weight "bold" :margin-left 5 :color colors/emerald}
               :type "outline"
               :button-style {:border-color colors/emerald}}]
      [button {:title "Book"
               :on-press #(rf/dispatch [:navigate-to-new-booking])
               :title-style {:font-weight "bold" :margin-horizontal 20}
               :button-style {:background-color colors/alizarin}}])]])

(defn render-bike-info
  [bike-data]
  [view {:style {:flex 1 :margin-bottom 30}}
   [bike-title/main bike-data]
   [view {:style {:margin-top 10}}
    [property-item/main "Manufacture year" (or (:manufacture-year bike-data) "-")]
    [property-item/main "Mileage" (or (:mileage bike-data) "-")]
    (when (:whatsapp bike-data)
      [property-item/with-icon "whatsapp" (:whatsapp bike-data)])]])

(defn main []
  (r/with-let [bike-data (rf/subscribe [:current-bike])]
    (let [get-reviews (:reviews @bike-data)
          bike-meta (meta @bike-data)]
      [view {:style {:flex 1}}
       [scroll-view {:style {:flex 1}}
        [safe-area-view {:style {:margin-bottom 10}}
         [bike-photos-swiper/main @bike-data]]
        [view {:style {:margin-horizontal 10}}
         [render-bike-info @bike-data]
         ; TODO: implement review
         ;(when-not (:loading? bike-meta) [reviews-list/main (get-reviews)])
         ]]
       (when-not (:loading? bike-meta)
         [render-bottom @bike-data])])))
