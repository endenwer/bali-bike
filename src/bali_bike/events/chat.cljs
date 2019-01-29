(ns bali-bike.events.chat
  (:require [bali-bike.edb :as edb]))

(defn get-another-user-for-chat
  [current-user chat]
  (let [another-user-uid (first (filter #(not= % (:uid current-user)) (:userUids chat)))]
    (get-in chat [:users (keyword another-user-uid)])))

;; events

(defn listen-chats-event
  [_ [_ _]]
  {:firestore/listen-chats :on-chats-updated})

(defn on-chats-updated-event
  [db [_ chats]]
  (let [current-user (:current-user db)
        modified-chats (map #(assoc % :another-user (get-another-user-for-chat current-user %))
                            chats)]
    (edb/insert-collection db :chats :list modified-chats)))

(defn navigate-to-chat-event
  [{:keys [db]} [_ id]]
  {:db (edb/insert-named-item db :chats :current {:id id})
   :navigation/navigate-to :chat})
