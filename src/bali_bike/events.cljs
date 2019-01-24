(ns bali-bike.events
  (:require
   [re-frame.core :refer [reg-event-db after] :as rf]
   [bali-bike.edb :as edb]
   [bali-bike.interceptors :as interceptors]
   [promesa.core :as p]
   [bali-bike.rn :as rn]
   [bali-bike.routing :as routing]
   [bali-bike.events.auth :as auth-events]
   [bali-bike.auth :as auth]
   [bali-bike.api :as api]
   [bali-bike.events.booking :as booking-events]))

(rf/reg-fx
 :navigation/navigate-to
 (fn [view-id]
   (routing/navigate-to view-id)))

(rf/reg-fx
 :navigation/navigate-back
 (fn []
   (routing/navigate-back)))

(rf/reg-event-fx
 :navigate-to
 (fn [_ [_ view-id]]
   {:navigation/navigate-to view-id}))

(rf/reg-event-fx
 :navigate-back
 (fn [_ _]
   {:navigation/navigate-back nil}))

(rf/reg-event-fx
 :navigate-to-bike
 (fn [{:keys [db]} [_ bike-id]]
   {:db (edb/insert-named-item db :bikes :current {:id bike-id} {:loading? true})
    :api/send-graphql {:query [:bike {:id bike-id} [:id :modelId :photos :price :rating
                                                    :reviewsCount :mileage :manufactureYear
                                                    :reviews [:id :rating :comment]]]
                       :callback-event :on-bike-loaded}
    :navigation/navigate-to :bike}))

(rf/reg-event-db
 :on-bike-loaded
 [interceptors/transform-event-to-kebab]
 (fn [db [_ {:keys [data]}]]
   (edb/insert-named-item db :bikes :current (:bike data) {:loading? false})))

(rf/reg-event-db
 :change-area-search-bar-text
 (fn [app-db [_ value]]
   (assoc app-db :area-search-bar-text value)))

(rf/reg-event-fx
 :set-area-filter-id
 (fn [{:keys [db]} [_ area-id]]
   {:db (-> db
            (assoc :area-filter-id area-id)
            (dissoc :area-search-bar-text))
    :navigation/navigate-back nil}))

(rf/reg-event-fx
 :set-dates-range
 (fn [{:keys [db]} [_ start-date end-date]]
   {:db (assoc db :dates-range {:start-date start-date :end-date end-date})
    :navigation/navigate-back nil}))

(rf/reg-event-db
 :on-bikes-loaded
 [interceptors/transform-event-to-kebab]
 (fn [db [_ {:keys [data]}]]
   (edb/insert-collection db :bikes :list (:bikes data) {:loading? false})))

(rf/reg-event-fx
 :load-bikes
 (fn [{:keys [db]} [_ _]]
   {:db (edb/insert-meta db :bikes :list {:loading? true})
    :api/send-graphql {:query [:bikes [:id :modelId :photos :price :rating :reviewsCount
                                       :mileage :manufactureYear]]
                       :callback-event :on-bikes-loaded}}))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   edb/initial-app-db))

;; auth handlers

(rf/reg-fx :auth/sign-in-with-google auth/sign-in-with-google)
(rf/reg-event-fx :signin-with-google auth-events/sign-in-with-google-event)
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
(rf/reg-event-fx :create-booking booking-events/create-booking-event)
(rf/reg-event-fx :update-delivery-region
                 [interceptors/transform-event-to-kebab]
                 booking-events/update-delivery-region-event)
(rf/reg-event-db :on-booking-created booking-events/on-booking-created-event)
(rf/reg-event-db :update-delivery-location booking-events/update-delivery-location-event)
(rf/reg-event-db :on-bookings-loaded
                 [interceptors/transform-event-to-kebab]
                 booking-events/on-bookings-loaded-event)

;; api
(rf/reg-fx :api/send-graphql api/send-graphql)
