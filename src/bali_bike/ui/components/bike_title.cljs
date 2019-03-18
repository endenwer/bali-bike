(ns bali-bike.ui.components.bike-title
  (:require [bali-bike.constants :as constants]
            [bali-bike.ui.components.common :refer [h3]]))

(defn main [bike]
  [h3
   (get constants/models (:model-id bike))])
