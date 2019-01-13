(ns bali-bike.ui.screens.login
  (:require [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]))

(defn main []
  [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
   [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}}
    "SETTINGS"]])
