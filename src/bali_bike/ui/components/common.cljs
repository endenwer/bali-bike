(ns bali-bike.ui.components.common
  (:require [bali-bike.rn :as rn]
            [bali-bike.colors :as colors]))

(defn button [props]
  [rn/button (merge {:border-radius 5} props)])

(defn h3 [style-props content]
  [rn/text (merge {:h3 true :style (merge {:color colors/wet-asphalt} style-props)})
   content])

(defn h4 [style-props content]
  [rn/text (merge {:h4 true :style (merge {:color colors/wet-asphalt} style-props)})
   content])

(defn text [{:keys [style] :as props} content]
  [rn/text (merge props {:style (merge {:color colors/wet-asphalt} style)})
   content])
