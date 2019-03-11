(ns bali-bike.ui.components.bike-rating
  (:require [bali-bike.rn :refer [view rating]]
            [bali-bike.ui.components.common :refer [text]]))

; TODO: implement rating
(defn main
  [bike]
  (if true
    [view]
    [view {:flex-direction "row" :align-items "center" :justify-content "flex-start"}
     [rating {:startingValue (:rating bike)
              :readonly true
              :imageSize 14}]
     [text {:style {:margin-left 5
                    :font-weight "bold"
                    :color "gray"
                    :font-size 14}} (:reviews-count bike)]]))
