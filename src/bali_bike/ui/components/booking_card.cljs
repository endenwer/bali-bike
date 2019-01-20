(ns bali-bike.ui.components.booking-card
  (:require [reagent.core :as r]
            [bali-bike.rn :refer [view image  touchable-highlight]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.constants :as constants]
            [re-frame.core :as rf]))

(defn main [booking-data]
  (r/with-let [get-bike (:bike booking-data)]
    (let [bike (get-bike)]
      [touchable-highlight
       {:on-press #(.log js/consle "on press")}
       [view {:flex 1 :margin-bottom 25}
        [text (get constants/models (:model-id bike))]]])))
