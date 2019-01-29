(ns bali-bike.firestore
  (:require [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(def message-listener (atom nil))
(def firestore (.firestore rn/firebase))

(defn listen-chats
  [callback-event]
  (let [chats-ref (.collection firestore "chats")]
    (.onSnapshot
     chats-ref
     (fn [snapshot]
       (rf/dispatch [callback-event
                     (map #(assoc (js->clj (.data %) :keywordize-keys true) :id (.-id %))
                          (.-docs snapshot))])))))

(defn listen-messages
  [{:keys [chat-id callback-event]}]
  (let [messages-ref (.orderBy
                      (.collection firestore (str "chats/" chat-id "/messages"))
                      "timestamp" "desc")]
    (reset!
     message-listener
     (.onSnapshot
      messages-ref
      (fn [snapshot]
        (rf/dispatch [callback-event
                      (map #(assoc (js->clj (.data %) :keywordize-keys true) :id (.-id %))
                           (.-docs snapshot))]))))))

(defn unlisten-messages []
  (@message-listener)
  (reset! message-listener nil))

(defn send-message
  [{:keys [text sender-uid chat-id]}]
  (let [batch (.batch firestore)
        message-ref (.doc (.collection firestore (str "chats/" chat-id "/messages")))
        chat-ref (.doc (.collection firestore "chats") chat-id)
        timestamp (.firestore.FieldValue.serverTimestamp rn/firebase)
        message {:text text :senderUid sender-uid :timestamp timestamp}]
    (-> batch
        (.set message-ref (clj->js message))
        (.update chat-ref #js {:lastMessage (:text message) :timestamp timestamp})
        (.commit))))
