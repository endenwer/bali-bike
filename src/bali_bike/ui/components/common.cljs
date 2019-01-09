(ns bali-bike.ui.components.common
  (:require [bali-bike.rn :as rn]))

(defn button [props]
  [rn/button (merge {:border-radius 5} props)])
