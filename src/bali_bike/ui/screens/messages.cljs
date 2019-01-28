(ns bali-bike.ui.screens.messages
  (:require [bali-bike.rn :refer [safe-area-view view avatar]]
            [bali-bike.ui.components.common :refer [h1 text]]
            [bali-bike.ui.components.chats-list :as chats-list]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/create-class
   {:component-did-mount #(rf/dispatch [:listen-chats])
    :render (fn []
              [safe-area-view {:style {:flex 1 :margin 10 :margin-top 30}}
               [h1 "Messages"]
               [chats-list/main]])}))
