(ns bali-bike.events.chat
  (:require [bali-bike.edb :as edb]
            [bali-bike.rn :as rn]))

(defn get-another-user-for-chat
  [current-user chat]
  (let [another-user-uid (first (filter #(not= % (:uid current-user)) (:userUids chat)))]
    (get-in chat [:users (keyword another-user-uid)])))

(defn format-message
  [chat message]
  (let [sender-uid (:senderUid message)]
    (merge message
           {:_id (:id message)
            :createdAt (rn/moment (:timestamp message))
            :user {:_id sender-uid
                   :name (get-in chat [:users (keyword sender-uid) :name])
                   :avatar (get-in chat [:users (keyword sender-uid) :photoURL])}})))

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
  {:db (-> db
           (edb/remove-collection :messages :list)
           (edb/insert-named-item :chats :current {:id id}))
   :navigation/navigate-to :chat})

(defn on-messages-updated-event
  [db [_ messages]]
  (let [chat (edb/get-named-item db :chats :current)
        formated-messages (map #(format-message chat %) messages)]
    (edb/insert-collection db :messages :list formated-messages)))

(defn listen-messages-event
  [{:keys [db]} [_ _]]
  (let [current-chat (edb/get-named-item db :chats :current)]
    {:firestore/listen-messages {:chat-id (:id current-chat)
                                 :callback-event :on-messages-updated}}))

(defn unlisten-messages-event
  [_ [_ _]]
  {:firestore/unlisten-messages nil})
