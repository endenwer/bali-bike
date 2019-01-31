(ns bali-bike.events.chat
  (:require [bali-bike.edb :as edb]
            [bali-bike.rn :as rn]))

(defn get-another-user-for-chat
  [current-user chat]
  (let [another-user-uid (first (filter #(not= % (:uid current-user)) (:userUids chat)))]
    (get-in chat [:users (keyword another-user-uid)])))

(defn format-message
  [chat message]
  (let [sender-uid (:senderUid message)
        timestamp (:timestamp message)]
    (merge message
           {:_id (:id message)
            :createdAt (if timestamp (rn/moment timestamp) (rn/moment))
            :user {:_id sender-uid
                   :name (get-in chat [:users (keyword sender-uid) :name])
                   :avatar (get-in chat [:users (keyword sender-uid) :photoURL])}})))

(defn get-chat-id
  [uids]
  (let [compare-result (apply compare uids)]
    (if (= compare-result -1)
      (apply str uids)
      (apply str (reverse uids)))))

(defn prepare-chat
  [user receiver]
  (let [user-uid (:uid user)
        receiver-uid (:uid receiver)]
    {:userUids [user-uid receiver-uid]
     :users {user-uid {:name (:display-name user)
                       :photoURL (:photo-url user)}
             receiver-uid {:name (:name receiver)
                           :photoURL (:photo-url receiver)}}}))

;; events

(defn listen-chats-event
  [{:keys [db]} [_ _]]
  (let [user (:current-user db)]
    {:firestore/listen-chats {:callback-event :on-chats-updated
                              :user-uid (:uid user)}}))

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

(defn navigate-to-chat-from-booking-event
  [{:keys [db]} [_ booking-id]]
  (let [user (:current-user db)
        booking (edb/get-item-by-id db :bookings booking-id)
        owner (:owner ((:bike booking)))
        chat-id (get-chat-id [(:uid user) (:uid owner)])
        chat (edb/get-item-by-id db :chats chat-id)]
    (if chat
      {:dispatch [:navigate-to-chat chat-id]}
      {:dispatch [:create-chat owner]})))

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

(defn send-message-event
  [{:keys [db]} [_ text]]
  (let [user (:current-user db)
        chat (edb/get-named-item db :chats :current)]
    {:firestore/send-message {:text text
                             :sender-uid (:uid user)
                             :chat-id (:id chat)}}))

(defn create-chat-event
  [{:keys [db]} [_ receiver]]
  (let [user (:current-user db)
        new-chat-id (get-chat-id [(:uid user) (:uid receiver)])
        new-chat (prepare-chat user receiver)]
    {:db (edb/prepend-collection db :chats :list [(assoc new-chat :id new-chat-id)])
     :dispatch [:navigate-to-chat new-chat-id]
     :firestore/create-chat {:id new-chat-id
                             :data new-chat}}))
