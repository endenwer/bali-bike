(ns bali-bike.firestore
  (:require [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(defn listen-chats [callback-event]
  (let [chats-ref (.collection (.firestore rn/firebase) "chats")]
    (.onSnapshot
     chats-ref
     (fn [snapshot]
       (rf/dispatch [callback-event
                     (map #(assoc (js->clj (.data %)) :id (.-id %)) (.-docs snapshot))])))))
