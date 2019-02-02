(ns bali-bike.ui.components.bike-price
  (:require [re-frame.core :as rf]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]))

(defn main
  [bike]
  [view {:style {:flex-direction "row"}}
   [text {:style {:font-weight "bold"}} (str "Rp" (:daily-price bike))]
   [text {:style {:font-weight "500"}} "/day"]])
