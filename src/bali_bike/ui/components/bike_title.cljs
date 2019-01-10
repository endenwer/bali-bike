(ns bali-bike.ui.components.bike-title
  (:require [bali-bike.constants :as constants]
            [bali-bike.ui.components.common :refer [h4]]))

(defn main [bike]
  [h4 {:font-weight "600"}
   (get constants/models (:model-id bike))])
