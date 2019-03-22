(ns bali-bike.notifications
  (:require [bali-bike.rn :as rn]
            [promesa.core :as p :refer-macros [alet]]
            [re-frame.core :as rf]
            [goog.object :as object]
            [reagent.core :as r]))

(defn request-permission []
  (alet [enabled (p/await (p/promise (.hasPermission (.messaging rn/firebase))))]
        (when-not enabled (.requestPermission (.messaging rn/firebase)))))

(defn get-fcm-token []
  (-> (.getToken (.messaging rn/firebase))
      (.then (fn [token] (rf/dispatch [:set-fcm-token token])))))

(defn on-refresh-fcm-token []
  (.onTokenRefresh (.messaging rn/firebase)
                   (fn [token] (rf/dispatch [:set-fcm-token token]))))

(defn handle-notification-event [event]
  (let [data (.-notification.data event)]
    (rf/dispatch [:notification-event-received (js->clj data)])))

(defn on-notification-opened []
  (.onNotificationOpened
   (.notifications rn/firebase)
   handle-notification-event))

(defn handle-initial-push-notification []
  (.then
   (.getInitialNotification (.notifications rn/firebase))
   (fn [event] (when event (handle-notification-event event)))))

(defn init []
  (on-refresh-fcm-token)
  (on-notification-opened))
