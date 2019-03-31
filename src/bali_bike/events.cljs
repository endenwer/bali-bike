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
   [bali-bike.events.notifications :as notification-events]
   [bali-bike.rn :as rn]))

(def global-interceptors [interceptors/leave-breadcrumb])

(defn event-db
  ([event handler] (rf/reg-event-db event global-interceptors handler))
  ([event interceptors handler]
   (rf/reg-event-db event (concat global-interceptors interceptors) handler)))

(defn event-fx
  ([event handler] (rf/reg-event-fx event global-interceptors handler))
  ([event interceptors handler]
   (rf/reg-event-fx event (concat global-interceptors interceptors) handler)))

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

(event-fx
 :navigate-to
 (fn [_ [_ view-id]]
   {:navigation/navigate-to view-id}))

(event-fx
 :navigate-back
 (fn [_ _]
   {:navigation/navigate-back nil}))

(event-fx
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

(event-fx
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

(event-fx
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


(event-fx
 :initialize-db
 (fn [_ _]
   {:db edb/initial-app-db
    :api/send-graphql {:query [:constants [:models [:id :value] :areas [:id :value]]]
                       :callback-event :on-constants-loaded}}))

(event-db
 :on-constants-loaded
 [interceptors/transform-event-to-kebab]
 (fn [db [_ {:keys [data]}]]
   (let [constants (:constants data)
         models (reduce #(assoc %1 (:id %2) (:value %2)) {} (:models constants))
         areas (reduce #(assoc %1 (:id %2) (:value %2)) {} (:areas constants))]
     (assoc db :constants {:models models :areas areas}))))

;; bike handlers
(event-fx :load-bikes bike-events/load-bikes-event)
(event-fx :load-saved-bikes bike-events/load-saved-bikes-event)
(event-fx :add-bike-to-saved bike-events/add-bike-to-saved-event)
(event-fx :remove-bike-from-saved bike-events/remove-bike-from-saved-event)
(event-fx :navigate-to-bike bike-events/navigate-to-bike-event)
(event-db
 :on-bike-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-bike-loaded-event)
(event-db
 :on-bikes-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-bikes-loaded-event)
(event-db
 :on-saved-bikes-loaded
 [interceptors/transform-event-to-kebab]
 bike-events/on-saved-bikes-loaded-event)

;; auth handlers

(rf/reg-fx :auth/sign-in-with-google auth/sign-in-with-google)
(rf/reg-fx :auth/sign-out auth/sign-out)
(rf/reg-fx :splash-screen/hide #(.hide rn/splash-screen))
(event-fx :user-signed-in auth-events/user-signed-in-event)
(event-fx :signin-with-google auth-events/sign-in-with-google-event)
(event-fx :sign-out auth-events/sign-out-event)
(event-fx :auth-state-changed
 [interceptors/transform-event-to-kebab]
 auth-events/auth-state-changed-event)

;; booking handlers

(rf/reg-fx :booking/load-delivery-address booking-events/load-delivery-address)
(event-fx :create-booking booking-events/create-booking-event)
(event-fx :load-bookings booking-events/load-bookings-event)
(event-fx :navigate-to-booking booking-events/navigate-to-booking-event)
(event-fx :set-delivery-location booking-events/set-delivery-location-event)
(event-fx :navigate-to-new-booking booking-events/navigate-to-new-booking-event)
(event-fx :set-new-booking-dates-range booking-events/set-new-booking-dates-range-event)
(event-fx :cancel-booking booking-events/cancel-booking-event)
(event-fx :confirm-booking booking-events/confirm-booking-event)
(event-fx :update-delivery-region
                 [interceptors/transform-event-to-kebab]
                 booking-events/update-delivery-region-event)
(event-fx :on-booking-created
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-booking-created-event)
(event-db :update-delivery-location booking-events/update-delivery-location-event)
(event-db :on-booking-loaded
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-booking-loaded-event)
(event-db :on-bookings-loaded
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-bookings-loaded-event)

;; api handlers
(rf/reg-fx :api/send-graphql api/send-graphql)

;; chats handlers

(event-fx :listen-chats chat-events/listen-chats-event)
(event-fx :navigate-to-chat chat-events/navigate-to-chat-event)
(event-fx :navigate-to-chat-from-booking chat-events/navigate-to-chat-from-booking-event)
(event-fx :listen-messages chat-events/listen-messages-event)
(event-fx :send-message chat-events/send-message-event)
(event-fx :unlisten-messages chat-events/unlisten-messages-event)
(event-fx :create-chat chat-events/create-chat-event)
(event-db :on-messages-updated chat-events/on-messages-updated-event)
(event-db :on-chats-updated chat-events/on-chats-updated-event)

;; firestore handlers

(rf/reg-fx :firestore/listen-chats firestore/listen-chats)
(rf/reg-fx :firestore/listen-messages firestore/listen-messages)
(rf/reg-fx :firestore/unlisten-messages firestore/unlisten-messages)
(rf/reg-fx :firestore/send-message firestore/send-message)
(rf/reg-fx :firestore/create-chat firestore/create-chat)
(rf/reg-fx :firestore/save-fcm-token firestore/save-fcm-token)
(rf/reg-fx :firestore/delete-fcm-token firestore/delete-fcm-token)

;; notifications

(rf/reg-fx :notifications/get-fcm-token notifications/get-fcm-token)
(rf/reg-fx :notifications/handle-initial-push-notification
           notifications/handle-initial-push-notification)
(rf/reg-fx :notifications/request-permission notifications/request-permission)

(event-fx :set-fcm-token notification-events/set-fcm-token-event)
(event-fx
 :notification-event-received
 [interceptors/transform-event-to-kebab]
 notification-events/notification-event-received-event)
