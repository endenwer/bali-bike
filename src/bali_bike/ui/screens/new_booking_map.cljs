(ns bali-bike.ui.screens.new-booking-map
  (:require [bali-bike.rn :refer [view map-view icon safe-area-view PROVIDER_GOOGLE]]
            [bali-bike.ui.components.common :refer [button]]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn render-map
  [{:keys [on-change]}]
  [view {:style {:flex 1}}
   [map-view {:style {:position "absolute"
                      :left 0
                      :right 0
                      :top 0
                      :bottom 0}
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

(defn render-bottom
  [{:keys [on-save loading?]}]
  [safe-area-view {:style {:padding 10 :align-self "stretch"}}
   [button {:title "Save"
            :loading loading?
            :disabled loading?
            :on-press on-save
            :background-color colors/turquoise
            :container-view-style {:margin-left 10
                                   :margin-right 10
                                   :margin 10}}]])

(defn main []
  (r/with-let [delivery-location (rf/subscribe [:delivery-location])
               change-region #(rf/dispatch [:update-delivery-region (js->clj %)])]
    [view {:style {:flex 1}}
     [render-map {:on-change change-region}]
     [render-bottom
      {:loading? (:loading? @delivery-location)
       :on-save
       #(rf/dispatch [:set-delivery-location])}]]))
