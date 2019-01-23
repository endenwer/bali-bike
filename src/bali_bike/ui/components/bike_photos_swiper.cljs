(ns bali-bike.ui.components.bike-photos-swiper
  (:require [bali-bike.rn :refer [view image]]
            [reagent.core :as r]))

(def Swiper (js/require "react-native-swiper"))
(def swiper (r/adapt-react-class Swiper))

(defn render-photo [photo-url image-styles]
  [view {:style {:flex 1 :align-items "center"}}
   [image {:style (merge {:flex 1
                          :background-color "skyblue"
                          :align-self "stretch"}
                         image-styles)
           :source {:uri photo-url}}]])

(defn main
  [{:keys [photos image-styles]}]
  [swiper {:showButtons true
           :loadMinimalSize 1
           :loop false
           :style {:height 250 :align-self "stretch"}}
   (for [photo-url photos]
     ^{:key photo-url} [render-photo photo-url image-styles])])
