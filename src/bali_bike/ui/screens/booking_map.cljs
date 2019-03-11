(ns bali-bike.ui.screens.booking-map
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.ui.components.map-view :as map-view]
            [bali-bike.ui.components.common :refer [button]]
            [bali-bike.colors :as colors]
            [bali-bike.rn :as rn :refer [view safe-area-view]]))

(defn open-map-app
  [latitude longitude]
  (let [scheme (.select rn/Platform #js {:ios "maps:0,0?q=" :android "geo:0,0?q="})
        lat-lng (str latitude "," longitude)
        label "Delivery location"
        url (.select rn/Platform #js {:ios (str scheme label "@" lat-lng)
                                      :android (str scheme lat-lng "(" label ")")})]
    (.openURL rn/Linking url)))

(defn render-bottom
  [{:keys [on-press]}]
  [safe-area-view {:style {:padding 10 :align-self "stretch"}}
   [button {:title "Open map"
            :on-press on-press
            :background-color colors/turquoise
            :button-style {:margin 10}}]])

(defn main []
  (r/with-let [booking-data (rf/subscribe [:current-booking])]
    (let [latitude (js/parseFloat (:delivery-location-latitude @booking-data))
          longitude (js/parseFloat (:delivery-location-longitude @booking-data))]
      [view {:style {:flex 1}}
       [map-view/main {:use-marker? true
                       :initial-region {:latitude latitude
                                        :longitude longitude
                                        :latitude-delta
                                        (js/parseFloat
                                         (:delivery-location-latitude-delta @booking-data))
                                        :longitude-delta
                                        (js/parseFloat
                                         (:delivery-location-longitude-delta @booking-data))}}]
       [render-bottom {:on-press #(open-map-app latitude longitude)}]])))
