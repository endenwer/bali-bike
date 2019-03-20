(ns bali-bike.ui.components.empty-icon
  (:require [bali-bike.rn :refer [image view]]))

(def empty-img (js/require "./images/empty.png"))

(defn main []
  [view {:style {:flex 1 :align-items "center" :justify-content "center"}}
   [image {:source empty-img :widht 256 :height 164}]])
