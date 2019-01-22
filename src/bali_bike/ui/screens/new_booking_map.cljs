(ns bali-bike.ui.screens.new-booking-map
  (:require [bali-bike.rn :refer [view map-view icon PROVIDER_GOOGLE]]
            [bali-bike.colors :as colors]))

(defn render-map []
  [map-view {:style {:position "absolute"
                     :left 0
                     :right 0
                     :top 0
                     :bottom 0}
             :showsUserLocation true
             :showsMyLocationButton true
             :provider PROVIDER_GOOGLE}])

(defn main []
  [view {:style {:flex 1}}
   [render-map]
   [view {:style {:left "50%"
                  :top "50%"
                  :position "absolute"
                  :margin-left -24
                  :margin-top -48}}
    [icon {:name "place"
           :color colors/alizarin
           :size 48}]]])
