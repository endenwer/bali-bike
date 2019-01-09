(ns bali-bike.ui.screens.bike
  (:require [bali-bike.rn :refer [text view scroll-view safe-area-view]]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.ui.components.property-item :as property-item]
            [bali-bike.ui.components.common :refer [button]]
            [bali-bike.colors :as colors]
            [bali-bike.ui.components.bike-rating :as bike-rating]
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
     [text {:style {:font-weight "bold"}}
      (str (:price bike-data) "K IDR per month")]
     [bike-rating/main bike-data]]
    [button {:title "Book"
             :background-color colors/alizarin
             :container-view-style {:margin-left 0 :margin-right 0}
             :text-style {:margin-horizontal 20 :font-weight "bold"}}]]])

(defn main []
  (r/with-let [bike-data (rf/subscribe [:current-bike])]
    [view {:style {:flex 1}}
     [scroll-view {:style {:flex 1}}
      [safe-area-view {:style {:margin-bottom 10}}
       [bike-photos-swiper/main (:photos @bike-data)]]
      [view {:style {:margin-horizontal 10}}
       [bike-title/main @bike-data]
       [property-item/main "Manufacture year" (:manufacture-year @bike-data)]
       [property-item/main "Mileage" (:mileage @bike-data)]]]
     [render-bottom @bike-data]]))
