(ns bali-bike.ui.screens.new-booking
  (:require [bali-bike.rn :refer [view safe-area-view scroll-view]]
            [bali-bike.ui.components.common :refer [text button]]
            [bali-bike.colors :as colors]
            [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(defn render-bottom []
  [safe-area-view {:style {:border-top-width 3
                           :border-color colors/clouds
                           :padding 10}}
   [button {:title "Book"
            :on-press #(rf/dispatch [:create-booking])
            :background-color colors/alizarin
            :container-view-style {:margin 10}
            :text-style {:margin-horizontal 20 :font-weight "bold"}}]])

(defn main []
  [view {:style {:flex 1}}
   [rn/map-view {:provider rn/PROVIDER_GOOGLE
                 :style {:height 400
                         :width 400}
                 :initial-region {:latitude 37.78825
                                  :longitude -122.4324
                                  :latitudeDelta 0.0922
                                  :longitudeDelta 0.0421}}]
   [render-bottom]])
