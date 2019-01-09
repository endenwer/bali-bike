(ns bali-bike.ui.components.bike-rating
  (:require [bali-bike.rn :refer [text view rating]]))

(defn main
  [bike]
  [view {:flex-direction "row" :align-items "center" :justify-content "flex-start"}
   [rating {:startingValue (:rating bike)
            :readonly true
            :imageSize 14}]
   [text {:style {:margin-left 5
                  :font-weight "bold"
                  :color "gray"
                  :font-size 14}} (:reviews-count bike)]])
