(ns bali-bike.ui.components.bike
  (:require [bali-bike.rn :refer [view image rating touchable-highlight icon]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.bike-photos-swiper :as bike-photos-swiper]
            [bali-bike.ui.components.bike-title :as bike-title]
            [bali-bike.ui.components.bike-rating :as bike-rating]
            [bali-bike.ui.components.bike-price :as bike-price]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]))

(defn render-save-button
  [{:keys [saved on-press]}]
  (let [icon-name (if saved "favorite" "favorite-border")
        color (if saved colors/alizarin colors/white)]
    [icon {:on-press on-press
           :name icon-name
           :reverse-color color
           :reverse true
           :container-style {:position "absolute"
                             :background-color "rgba(0, 0, 0, 0.2)"
                             :right 0}}]))

(defn main [bike-data]
  [touchable-highlight
   {:on-press #(rf/dispatch [:navigate-to-bike (:id bike-data)])}
   [view {:flex 1 :margin-bottom 25}
    [bike-photos-swiper/main {:photos (:photos bike-data) :image-styles {:border-radius 5}}]
    [render-save-button {:saved (:saved bike-data)
                         :on-press
                         #(rf/dispatch
                           [(if (:saved bike-data) :remove-bike-from-saved :add-bike-to-saved)
                            (:id bike-data)])}]
    [bike-title/main bike-data]
    [bike-rating/main bike-data]
    [bike-price/main bike-data]]])
