(ns bali-bike.ui.components.bike-title
  (:require [bali-bike.constants :as constants]
            [bali-bike.ui.components.common :refer [h2]]))

(defn main [bike]
  [h2
   (get constants/models (:model-id bike))])
