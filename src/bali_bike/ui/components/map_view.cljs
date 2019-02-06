(ns bali-bike.ui.components.map-view
  (:require [bali-bike.rn :refer [view icon map-view PROVIDER_GOOGLE]]
            [bali-bike.colors :as colors]))

(defn main
  [{:keys [on-change initial-region]}]
  [view {:style {:flex 1}}
   [map-view {:style {:position "absolute"
                      :left 0
                      :right 0
                      :top 0
                      :bottom 0}
              :initiaRegion initial-region
              :onRegionChangeComplete on-change
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
