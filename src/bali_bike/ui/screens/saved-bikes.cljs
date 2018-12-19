(ns bali-bike.ui.screens.saved-bikes
  (:require [bali-bike.rn :refer [text view]]))

(defn main []
  [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
   [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}}
    "SAVED"]])
