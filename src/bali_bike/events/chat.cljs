(ns bali-bike.events.chat
  (:require [bali-bike.edb :as edb]))

(defn listen-chats-event
  [{::keys [db]} [_ _]]
  {:firestore/listen-chats :on-chats-updated})

(defn on-chats-updated-event
  [db [_ chats]]
  (edb/insert-collection db :chats :list chats))
