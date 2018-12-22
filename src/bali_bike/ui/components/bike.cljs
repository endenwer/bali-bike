(ns bali-bike.ui.components.bike
  (:require [reagent.core :as r]
            [bali-bike.rn :refer [text view image rating]]))

(def Swiper (js/require "react-native-swiper"))
(def swiper (r/adapt-react-class Swiper))

(defn render-photo [photo-url]
  [view {:style {:flex 1 :align-items "center" :background-color "powderblue"}}
   [image {:style {:flex 1 :background-color "skyblue" :align-self "stretch"}
           :source {:uri photo-url}}]])

(defn render-photos [photos]
  [swiper {:showButtons true
           :loadMinimalSize 1
           :loop false
           :style {:height 250 :align-self "stretch"}}
   (for [photo-url photos]
     ^{:key photo-url} [render-photo photo-url])])

(defn main [bike-data]
  (.log js/console "HELLo")
  [view {:flex 1 :margin-bottom 25}
   [render-photos (:photos bike-data)]
   [view {:style {:margin-horizontal 10}}
    [text {:style {:font-weight "600" :font-size 25}} (:name bike-data)]
    [view {:flex-direction "row" :align-items "center" :justify-content "flex-start"}
     [rating {:startingValue (:rating bike-data)
              :readonly true
              :imageSize 12}]
     [text {:style {:margin-left 5
                    :color "gray"
                    :font-size 12}} (:reviews-count bike-data)]]
    [text {:style {:margin-top 5}} (str (:price bike-data) "K IDR per month")]]])
