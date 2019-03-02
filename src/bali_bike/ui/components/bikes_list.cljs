(ns bali-bike.ui.components.bikes-list
  (:require [reagent.core :as r]
            [bali-bike.ui.components.bike :as bike]
            [bali-bike.colors :as colors]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.full-screen-loading :as full-screen-loading]
            [bali-bike.rn :refer [scroll-view activity-indicator view flat-list]]
            [re-frame.core :as rf]))

(defn render-list-bottom
  [loading?]
  [view {:style {:flex 1
                 :padding-bottom 10
                 :height 30
                 :align-items "center"
                 :justify-content "center"}}
   (when loading?
     [activity-indicator {:size "small" :color colors/turquoise}])])

(defn main
  [sub-name]
  (r/with-let [bikes (rf/subscribe [sub-name])
               bikes-meta (rf/subscribe [(keyword (str (name sub-name) "-meta"))])]
    [view {:style {:flex 1 :padding-top 20}}
     (if (and (= 0 (count @bikes)) (:loading? @bikes-meta))
       [full-screen-loading/main]
       [flat-list {:data @bikes
                   :bounces false
                   :shows-vertical-scroll-indicator false
                   :key-extractor #(.-id %)
                   :List-footer-component #(r/as-element
                                            [render-list-bottom (:loading? @bikes-meta)])
                   :render-item #(r/as-element
                                  [bike/main (js->clj (.-item %) :keywordize-keys true)])
                   :on-end-reached-threshold 0.01
                   :on-end-reached #(rf/dispatch [:load-bikes])}])]))
