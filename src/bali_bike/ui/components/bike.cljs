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
           :color color
           :underlay-color "rgba(0,0,0,0.2)"
           :container-style {:position "absolute"
                             :margin 10
                             :padding 15
                             :border-radius 30
                             :background-color "rgba(0,0,0,0.2)"
                             :right 0}}]))

(defn main [bike-data]
  [view {:flex 1 :margin-bottom 15 :margin-top 10}
   [bike-photos-swiper/main {:photos (:photos bike-data)
                             :on-press #(rf/dispatch [:navigate-to-bike (:id bike-data)])
                             :image-styles {:border-radius 5}}]
   [render-save-button {:saved (:saved bike-data)
                        :on-press
                        #(rf/dispatch
                          [(if (:saved bike-data) :remove-bike-from-saved :add-bike-to-saved)
                           (:id bike-data)])}]
   [touchable-highlight
    {:on-press #(rf/dispatch [:navigate-to-bike (:id bike-data)])
     :underlay-color "transparent"}
    [view {:style {:margin-top 5 :margin-left 5}}
     [bike-title/main bike-data]
     [bike-rating/main bike-data]
     [bike-price/main {:bike bike-data :bold? false}]]]])
