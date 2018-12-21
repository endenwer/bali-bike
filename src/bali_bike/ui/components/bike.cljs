(ns bali-bike.ui.components.bike
  (:require [reagent.core :as r]
            [bali-bike.rn :refer [text view image]]))

(def Swiper (js/require "react-native-swiper"))
(def swiper (r/adapt-react-class Swiper))

(defn main [bike-data]
  [swiper {:showButtons true
           :loadMinimalSize 1
           :loop false
           :style {:height 400}}
   (for [photo-url (:photos bike-data)]
     ^{:key photo-url} [view {:style {:flex 1 :align-items "center" :background-color "powderblue"}}
                        [image {:style {:flex 1 :background-color "skyblue" :align-self "stretch"}
                                :source {:uri photo-url}}]])])
