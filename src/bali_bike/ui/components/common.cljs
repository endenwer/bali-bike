(ns bali-bike.ui.components.common
  (:require [bali-bike.rn :as rn]
            [bali-bike.colors :as colors]))

(defn button [props]
  [rn/button (merge {:border-radius 5} props)])

(defn h1
  ([content] (h1 {} content))
  ([style-props content]
   (let [default-styles {:color colors/wet-asphalt :font-weight "600" :font-size 32}]
     [rn/text {:style (merge default-styles style-props)} content])))

(defn h2
  ([content] (h2 {} content))
  ([style-props content]
   (let [default-styles {:color colors/wet-asphalt :font-weight "600" :font-size 28}]
     [rn/text {:style (merge default-styles style-props)} content])))

(defn h3
  ([content] (h3 {} content))
  ([style-props content]
   (let [default-styles {:color colors/wet-asphalt :font-weight "600" :font-size 22}]
     [rn/text {:style (merge default-styles style-props)} content])))

(defn text
  ([content] (text {} content))
  ([{:keys [style] :as props} content]
   [rn/text (merge props {:style (merge {:color colors/wet-asphalt} style)})
    content]))
