(ns bali-bike.ui.components.map-view
  (:require [bali-bike.rn :refer [view icon map-view map-marker PROVIDER_GOOGLE]]
            [bali-bike.constants :as constants]
            [bali-bike.colors :as colors]))

(defn main
  [{:keys [on-change initial-region use-marker?]}]
  [view {:style {:flex 1}}
   [map-view {:style {:position "absolute"
                      :left 0
                      :right 0
                      :top 0
                      :bottom 0}
              :initialRegion (or initial-region constants/default-region)
              :onRegionChangeComplete on-change
              :showsUserLocation true
              :showsMyLocationButton true
              :provider PROVIDER_GOOGLE}
    (when use-marker?
      [map-marker {:coordinate {:latitude (:latitude initial-region)
                                :longitude (:longitude initial-region)}
                   :title "Title"}])]
   (when-not use-marker?
     [view {:style {:left "50%"
                    :top "50%"
                    :position "absolute"
                    :margin-left -24
                    :margin-top -44}}
      [icon {:name "place"
             :color colors/alizarin
             :size 48}]])])
