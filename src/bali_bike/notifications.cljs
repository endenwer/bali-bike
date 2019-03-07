(ns bali-bike.notifications
  (:require [bali-bike.rn :as rn]
            [promesa.core :as p :refer-macros [alet]]
            [re-frame.core :as rf]
            [goog.object :as object]))

(defn request-permission []
  (alet [enabled (p/await (p/promise (.hasPermission (.messaging rn/firebase))))]
        (when enabled (.requestPermission (.messaging rn/firebase)))))

(defn get-fcm-token []
  (-> (.getToken (.messaging rn/firebase))
      (.then (fn [token] (rf/dispatch [:set-fcm-token token])))))

(defn on-refresh-fcm-token []
  (.onTokenRefresh (.messaging rn/firebase)
                   (fn [token] (rf/dispatch [:set-fcm-token token]))))

(defn parse-notification-payload [s]
  (try
    (js/JSON.parse s)
    (catch :default _
      #js {})))

(defn handle-notification-event [event]
  (let [msg (object/get (.. event -notification -data) "msg")
        data (parse-notification-payload msg)]
    (rf/dispatch [:notification-event-received data])))

(defn on-notification-opened []
  (.. rn/firebase
      notifications
      (onNotificationOpened handle-notification-event)))

(defn handle-initial-push-notification []
  (.. rn/firebase
      notifications
      getInitialNotification
      (then (fn [event]
              (when event
                (handle-notification-event event))))))

(defn init []
  (on-refresh-fcm-token)
  (on-notification-opened))
