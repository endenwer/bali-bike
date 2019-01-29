(ns bali-bike.firestore
  (:require [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(def message-listener (atom nil))

(defn listen-chats
  [callback-event]
  (let [chats-ref (.collection (.firestore rn/firebase) "chats")]
    (.onSnapshot
     chats-ref
     (fn [snapshot]
       (rf/dispatch [callback-event
                     (map #(assoc (js->clj (.data %) :keywordize-keys true) :id (.-id %))
                          (.-docs snapshot))])))))

(defn listen-messages
  [{:keys [chat-id callback-event]}]
  (let [messages-ref (.orderBy
                      (.collection (.firestore rn/firebase) (str "chats/" chat-id "/messages"))
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
  (let [messages-ref (.collection (.firestore rn/firebase) (str "chats/" chat-id "/messages"))
        timestamp (.firestore.FieldValue.serverTimestamp rn/firebase)
        message #js {:text text :senderUid sender-uid :timestamp timestamp}]
    (.add messages-ref message)))
