(ns bali-bike.events
  (:require
   [re-frame.core :refer [reg-event-db after] :as rf]
   [clojure.spec.alpha :as s]
   [bali-bike.db :as db :refer [app-db]]
   [bali-bike.routing :as routing]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(rf/reg-fx
 ::navigate-to
 (fn [view-id]
   (routing/navigate-to view-id)))

(rf/reg-fx
 ::navigate-back
 (fn []
   (routing/navigate-back)))

(rf/reg-event-fx
 :navigate-to
 (fn [_ [_ view-id]]
   {::navigate-to view-id}))

(rf/reg-event-fx
 :navigate-back
 (fn [_ _]
   {::navigate-back nil}))

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   app-db))
