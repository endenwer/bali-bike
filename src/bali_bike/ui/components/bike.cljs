(ns bali-bike.ui.components.bike
  (:require [reagent.core :as r]
            [bali-bike.rn :refer [view image rating touchable-highlight]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.constants :as constants]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.ui.components.bike-rating :as bike-rating]
            [re-frame.core :as rf]))

(defn main [bike-data]
  [touchable-highlight
   {:on-press #(rf/dispatch [:navigate-to-bike (:id bike-data)])}
   [view {:flex 1 :margin-bottom 25}
    [bike-photos-swiper/main (:photos bike-data)]
    [bike-title/main bike-data]
    [bike-rating/main bike-data]
    [text {:style {:margin-top 5}} (str (:price bike-data) "K IDR per month")]]])
