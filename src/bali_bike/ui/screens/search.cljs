(ns bali-bike.ui.screens.search
  (:require [bali-bike.rn :refer [text view]]
            [bali-bike.ui.components.bikes-list :as bikes-list]))

(defn main []
  [view {:style {:flex 1 :flex-direction "column" :align-items "center"}}
   [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
    [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}}
     "SEARCH"]]
   [bikes-list/main]])
