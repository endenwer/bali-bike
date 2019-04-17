(ns bali-bike.ui.components.bike-photos-swiper
  (:require [bali-bike.rn :refer [view image touchable-highlight]]
            [reagent.core :as r]
            [bali-bike.colors :as colors]))

(def Swiper (js/require "react-native-swiper"))
(def swiper (r/adapt-react-class Swiper))

(defn render-photo
  [photo-url image-styles on-press]
  [touchable-highlight {:on-press on-press :underlay-color "transparent" :style {:flex 1}}
   [view {:style {:flex 1 :align-items "center"}}
    [image {:style (merge {:flex 1
                           :background-color colors/clouds
                           :align-self "stretch"}
                          image-styles)
            :source {:uri photo-url}}]]])

(defn main
  [{:keys [photos image-styles on-press]}]
  [swiper {:showButtons true
           :loadMinimal true
           :loadMinimalSize 1
           :loop false
           :style {:height 250 :align-self "stretch"}}
   (for [photo-url photos]
     ^{:key photo-url} [render-photo photo-url image-styles on-press])])
