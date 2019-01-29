(ns bali-bike.ui.components.chat-preview
  (:require [bali-bike.rn :refer [view avatar]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.colors :as colors]
            [bali-bike.rn :as rn]))

(defn render-avatar
  [photo-url]
  (if photo-url
    [avatar {:rounded true :medium true :source {:uri photo-url}}]
    [avatar {:rounded true :medium true :title "SL"}]))

(defn main
  [chat]
  [view {:style {:flex-direction "row"
                 :justify-content "space-between"
                 :margin-top 10}}
   [render-avatar (get-in chat [:another-user :photoURL])]
   [view {:style {:flex 1
                  :margin-left 10
                  :padding-bottom 5
                  :border-color colors/clouds
                  :border-bottom-width 1}}
    [view {:style {:flex 1 :justify-content "space-between" :flex-direction "row"}}
     [text {:number-of-lines 1
            :style {:font-weight "bold"
                    :margin-bottom 5
                    :flex 1}}
      (get-in chat [:another-user :name])]
     [text {:style {:color colors/concrete}} (.calendar (rn/moment (:timestamp chat)))]]
    [text {:number-of-lines 1 :style {:flex 1}} (:lastMessage chat)]]])
