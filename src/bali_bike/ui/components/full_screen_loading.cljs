(ns bali-bike.ui.components.full-screen-loading
  (:require [bali-bike.rn :refer [activity-indicator view]]
            [bali-bike.colors :as colors]))

(defn main []
  [view {:style {:flex 1 :align-items "center" :justify-content "center"}}
   [activity-indicator {:size "large" :color colors/turquoise}]])
