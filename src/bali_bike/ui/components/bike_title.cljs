(ns bali-bike.ui.components.bike-title
  (:require [bali-bike.ui.components.common :refer [h3]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main [bike]
  (r/with-let [constants (rf/subscribe [:constants])]
    [h3 (get-in @constants [:models (:model-id bike)])]))
