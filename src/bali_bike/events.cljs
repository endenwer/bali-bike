(ns bali-bike.events
  (:require
   [re-frame.core :refer [reg-event-db after] :as rf]
   [bali-bike.edb :as edb]
   [bali-bike.interceptors :as interceptors]
   [bali-bike.routing :as routing]
   [bali-bike.events.auth :as auth-events]
   [bali-bike.auth :as auth]
   [bali-bike.api :as api]
   [bali-bike.events.booking :as booking-events]
   [bali-bike.events.bike :as bike-events]
   [bali-bike.events.chat :as chat-events]
   [bali-bike.firestore :as firestore]
   [bali-bike.notifications :as notifications]
   [bali-bike.events.notifications :as notification-events]))

(rf/reg-fx
 :navigation/navigate-to
 (fn [view-id]
   (routing/navigate-to view-id)))

(rf/reg-fx
 :navigation/navigate-back
 (fn []
   (routing/navigate-back)))

(rf/reg-fx
 :navigation/replace
 (fn [view-id]
   (routing/replace-route view-id)))

(rf/reg-event-fx
 :navigate-to
 (fn [_ [_ view-id]]
   {:navigation/navigate-to view-id}))

(rf/reg-event-fx
 :navigate-back
 (fn [_ _]
   {:navigation/navigate-back nil}))

(rf/reg-event-db
 :change-area-search-bar-text
 (fn [app-db [_ value]]
   (assoc app-db :area-search-bar-text value)))

(rf/reg-event-fx
 :set-area-filter-id
 (fn [{:keys [db]} [_ area-id]]
   (let [current-area-id (:area-filter-id db)]
     (if (= area-id current-area-id)
       {:db (dissoc db :area-search-bar-text)
        :navigation/navigate-back nil}
       {:db (-> (edb/remove-collection db :bikes :list)
                (dissoc :area-search-bar-text)
                (assoc :area-filter-id area-id))
        :navigation/navigate-back nil
        :dispatch [:load-bikes]}))))

(rf/reg-event-db
 :change-model-search-bar-text
 (fn [app-db [_ value]]
   (assoc app-db :model-search-bar-text value)))

(rf/reg-event-fx
 :set-model-filter-id
 (fn [{:keys [db]} [_ model-id]]
   (let [current-model-id (:model-filter-id db)]
     (if (= model-id current-model-id)
       {:db (dissoc db :model-search-bar-text)
        :navigation/navigate-back nil}
       {:db (-> (edb/remove-collection db :bikes :list)
                (dissoc :model-search-bar-text)
                (assoc :model-filter-id model-id))
        :navigation/navigate-back nil
        :dispatch [:load-bikes]}))))

(rf/reg-event-fx
 :set-dates-range
 (fn [{:keys [db]} [_ start-date end-date]]
   (let [dates-range (:dates-range db)
         new-dates-range {:start-date start-date :end-date end-date}]
     (if (= dates-range new-dates-range)
       {:navigation/navigate-back nil}
       {:db (-> (edb/remove-collection db :bikes :list)
                (assoc :dates-range new-dates-range))
        :navigation/navigate-back nil
        :dispatch [:load-bikes]}))))


(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   edb/initial-app-db))

;; bike handlers
(rf/reg-event-fx :load-bikes bike-events/load-bikes-event)
(rf/reg-event-fx :load-saved-bikes bike-events/load-saved-bikes-event)
(rf/reg-event-fx :add-bike-to-saved bike-events/add-bike-to-saved-event)
(rf/reg-event-fx :remove-bike-from-saved bike-events/remove-bike-from-saved-event)
(rf/reg-event-fx :navigate-to-bike bike-events/navigate-to-bike-event)
(rf/reg-event-db
 :on-bike-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-bike-loaded-event)
(rf/reg-event-db
 :on-bikes-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-bikes-loaded-event)
(rf/reg-event-db
 :on-saved-bikes-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-saved-bikes-loaded-event)

;; auth handlers

(rf/reg-fx :auth/sign-in-with-google auth/sign-in-with-google)
(rf/reg-fx :auth/sign-out auth/sign-out)
(rf/reg-event-fx :user-signed-in auth-events/user-signed-in-event)
(rf/reg-event-fx :signin-with-google auth-events/sign-in-with-google-event)
(rf/reg-event-fx :sign-out auth-events/sign-out-event)
(rf/reg-event-fx :auth-state-changed
 [interceptors/transform-event-to-kebab]
 auth-events/auth-state-changed-event)

;; booking handlers

(rf/reg-fx :booking/load-delivery-address booking-events/load-delivery-address)
(rf/reg-event-fx :create-booking booking-events/create-booking-event)
(rf/reg-event-fx :load-bookings booking-events/load-bookings-event)
(rf/reg-event-fx :navigate-to-booking booking-events/navigate-to-booking-event)
(rf/reg-event-fx :set-delivery-location booking-events/set-delivery-location-event)
(rf/reg-event-fx :navigate-to-new-booking booking-events/navigate-to-new-booking-event)
(rf/reg-event-fx :set-new-booking-dates-range booking-events/set-new-booking-dates-range-event)
(rf/reg-event-fx :cancel-booking booking-events/cancel-booking-event)
(rf/reg-event-fx :confirm-booking booking-events/confirm-booking-event)
(rf/reg-event-fx :update-delivery-region
                 [interceptors/transform-event-to-kebab]
                 booking-events/update-delivery-region-event)
(rf/reg-event-fx :on-booking-created
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-booking-created-event)
(rf/reg-event-db :update-delivery-location booking-events/update-delivery-location-event)
(rf/reg-event-db :on-booking-loaded
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-booking-loaded-event)
(rf/reg-event-db :on-bookings-loaded
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-bookings-loaded-event)

;; api handlers
(rf/reg-fx :api/send-graphql api/send-graphql)

;; chats handlers

(rf/reg-event-fx :listen-chats chat-events/listen-chats-event)
(rf/reg-event-fx :navigate-to-chat chat-events/navigate-to-chat-event)
(rf/reg-event-fx :navigate-to-chat-from-booking chat-events/navigate-to-chat-from-booking-event)
(rf/reg-event-fx :listen-messages chat-events/listen-messages-event)
(rf/reg-event-fx :send-message chat-events/send-message-event)
(rf/reg-event-fx :unlisten-messages chat-events/unlisten-messages-event)
(rf/reg-event-fx :create-chat chat-events/create-chat-event)
(rf/reg-event-db :on-messages-updated chat-events/on-messages-updated-event)
(rf/reg-event-db :on-chats-updated chat-events/on-chats-updated-event)

;; firestore handlers

(rf/reg-fx :firestore/listen-chats firestore/listen-chats)
(rf/reg-fx :firestore/listen-messages firestore/listen-messages)
(rf/reg-fx :firestore/unlisten-messages firestore/unlisten-messages)
(rf/reg-fx :firestore/send-message firestore/send-message)
(rf/reg-fx :firestore/create-chat firestore/create-chat)

;; notifications

(rf/reg-fx :notifications/get-fcm-token notifications/get-fcm-token)
(rf/reg-fx :notifications/handle-initial-push-notification
           notifications/handle-initial-push-notification)
(rf/reg-fx :notifications/request-permission notifications/request-permission)

(rf/reg-event-fx :set-fcm-token notification-events/set-fcm-token-event)
(rf/reg-event-fx :notification-event-received notification-events/notification-event-received-event)
