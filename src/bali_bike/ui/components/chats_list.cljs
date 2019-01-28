(ns bali-bike.ui.components.chats-list
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.rn :refer [scroll-view]]
            [bali-bike.ui.components.chat-preview :as chat-preview]))

(defn main []
  (r/with-let [chats (rf/subscribe [:chats])]
    [scroll-view {:style {:flex 1} :showsVerticalScrollIndicator false}
     (for [chat @chats]
       ^{:key (:id chat)} [chat-preview/main chat])]))
