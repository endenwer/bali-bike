(ns bali-bike.ui.components.chats-list
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.rn :refer [scroll-view]]
            [bali-bike.ui.components.empty-icon :as empty-icon]
            [bali-bike.ui.components.chat-preview :as chat-preview]))

(defn main []
  (r/with-let [chats (rf/subscribe [:chats])]
    (if (= 0 (count @chats))
      [empty-icon/main]
      [scroll-view {:style {:flex 1} :showsVerticalScrollIndicator false}
       (for [chat @chats]
         ^{:key (:id chat)} [chat-preview/main chat])])))
