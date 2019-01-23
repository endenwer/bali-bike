(ns bali-bike.ui.screens.new-booking-map
  (:require [bali-bike.rn :refer [view map-view icon safe-area-view PROVIDER_GOOGLE]]
            [bali-bike.ui.components.common :refer [button]]
            [bali-bike.colors :as colors]))

(defn render-map []
  [view {:style {:flex 1}}
   [map-view {:style {:position "absolute"
                      :left 0
                      :right 0
                      :top 0
                      :bottom 0}
              :showsUserLocation true
              :showsMyLocationButton true
              :provider PROVIDER_GOOGLE}]
   [view {:style {:left "50%"
                  :top "50%"
                  :position "absolute"
                  :margin-left -24
                  :margin-top -44}}
    [icon {:name "place"
           :color colors/alizarin
           :size 48}]]])

(defn render-bottom [bike-data]
  [safe-area-view {:style {:padding 10 :align-self "stretch"}}
   [button {:title "Save"
            :on-press #(.log js/console "save location")
            :background-color colors/turquoise
            :container-view-style {:margin-left 10
                                   :margin-right 10
                                   :margin 10}}]])

(defn main []
  [view {:style {:flex 1}}
   [render-map]
   [render-bottom]])
