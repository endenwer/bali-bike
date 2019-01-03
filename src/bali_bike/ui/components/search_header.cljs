(ns bali-bike.ui.components.search-header
  (:require [bali-bike.rn :refer [text view touchable-highlight]]
            [re-frame.core :as rf]))

(def button-styles
  {:background-color "#7f8c8d" :padding 5 :margin-horizontal 5})

(defn main []
  [touchable-highlight
   {:on-press #(rf/dispatch [:navigate-to :area-filter])}
   [view {:style {:flex-direction "row"
                  :padding-horizontal 20
                  :padding-vertical 30
                  :align-self "stretch"
                  :align-items "center"
                  :background-color "#ecf0f1"}}
    [text {:style button-styles} "Any area"]
    [text {:style button-styles} "Any model"]
    [text {:style button-styles} "Any dates"]
    [text {:style button-styles} "Filters"]]])
