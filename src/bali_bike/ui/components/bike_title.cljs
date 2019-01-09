(ns bali-bike.ui.components.bike-title
  (:require [bali-bike.constants :as constants]
            [bali-bike.rn :refer [text]]))

(defn main [bike]
  [text {:style {:font-weight "600" :font-size 25}}
   (get constants/models (:model-id bike))])
