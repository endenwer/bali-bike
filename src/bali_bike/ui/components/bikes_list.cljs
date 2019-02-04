(ns bali-bike.ui.components.bikes-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.bike :as bike]
            [bali-bike.colors :as colors]
            [bali-bike.rn :refer [scroll-view activity-indicator view flat-list]]
            [re-frame.core :as rf]))

(defn render-loading []
  [view {:style {:flex 1 :align-items "center" :justify-content "center"}}
   [activity-indicator {:size "large" :color colors/turquoise}]])

(defn main
  [sub-name]
  (r/with-let [bikes (rf/subscribe [sub-name])
               bikes-meta (rf/subscribe [(keyword (str (name sub-name) "-meta"))])]
    [view {:style {:flex 1 :padding-top 20}}
     [flat-list {:data @bikes
                 :bounces false
                 :shows-vertical-scroll-indicator false
                 :key-extractor #(.-id %)
                 :list-footer-component (r/reactify-component render-loading)
                 :render-item #(r/as-element
                                [bike/main (js->clj (.-item %) :keywordize-keys true)])
                 :on-end-reached-threshold 0.3
                 :on-end-reached #(rf/dispatch [:load-bikes])}]]))
