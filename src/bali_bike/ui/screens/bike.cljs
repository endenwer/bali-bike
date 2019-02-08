(ns bali-bike.ui.screens.bike
  (:require [bali-bike.rn :refer [view scroll-view safe-area-view rating]]
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
     [bike-price/main bike-data]
     [bike-rating/main bike-data]]
    [button {:title "Book"
             :on-press #(rf/dispatch [:navigate-to-new-booking])
             :title-style {:font-weight "bold" :margin-horizontal 20}
             :button-style {:background-color colors/alizarin}}]]])

(defn render-bike-info
  [bike-data]
  [view {:style {:flex 1 :margin-bottom 30}}
   [bike-title/main bike-data]
   [property-item/main "Manufacture year" (:manufacture-year bike-data)]
   [property-item/main "Mileage" (:mileage bike-data)]])

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
         (when-not (:loading? bike-meta)
           [reviews-list/main (get-reviews)])]]
       [render-bottom @bike-data]])))
