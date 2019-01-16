(ns bali-bike.ui.screens.new-booking
  (:require [bali-bike.rn :refer [view safe-area-view scroll-view]]
            [bali-bike.ui.components.common :refer [text button]]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]))

(defn render-bottom []
  [safe-area-view {:style {:border-top-width 3
                           :border-color colors/clouds
                           :padding 10}}
   [button {:title "Book"
            :on-press #(rf/dispatch [:create-booking])
            :background-color colors/alizarin
            :container-view-style {:margin 10}
            :text-style {:margin-horizontal 20 :font-weight "bold"}}]])

(defn main []
  [view {:style {:flex 1}}
   [scroll-view {:style {:flex 1}}]
   [render-bottom]])
