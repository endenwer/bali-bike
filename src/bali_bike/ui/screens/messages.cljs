(ns bali-bike.ui.screens.messages
  (:require [bali-bike.rn :refer [safe-area-view view avatar]]
            [bali-bike.ui.components.common :refer [h1 text]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn render-chat-preview []
  [view {:style {:flex-direction "row"
                 :justify-content "space-between"
                 :margin-top 10}}
   [avatar {:rounded true :medium true :title "SL"}]
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
      "Stepan Lusnikov"]
     [text {:style {:color colors/concrete}} "09:31"]]
    [text {:number-of-lines 1 :style {:flex 1}} "Facere omnis voluptatum illo ut. Quod molestias voluptatibus sint quia quidem. Quaerat qui aut molestias sed repudiandae. Dolores architecto omnis totam iure illo."]]])

(defn render-chats []
  [view {:style {:flex 1}}
   (for [i [1 2 3]]
     ^{:key i} [render-chat-preview])])

(defn main []
  (r/create-class
   {:component-did-mount #(rf/dispatch [:load-saved-bikes])
    :render (fn []
              [safe-area-view {:style {:flex 1 :margin 10 :margin-top 30}}
               [h1 "Messages"]
               [render-chats]])}))
