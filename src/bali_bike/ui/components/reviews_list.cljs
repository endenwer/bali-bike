(ns bali-bike.ui.components.reviews-list
  (:require [bali-bike.ui.components.common :refer [text h2]]
            [bali-bike.rn :refer [view] :as rn]
            [bali-bike.colors :as colors]))

(defn render-review
  [{:keys [rating comment]}]
  [view {:style {:flex 1 :margin-bottom 20}}
   [rn/rating {:starting-value rating :readonly true :image-size 14}]
   (if comment
     [text comment]
     [text {:style {:color colors/silver}} "(no comment)"])])

(defn main
  [reviews]
  [view {:style {:flex 1}}
   [h2 {:margin-bottom 10} "Reviews"]
   (for [review-data reviews]
     ^{:key (:id review-data)} [render-review review-data])])
