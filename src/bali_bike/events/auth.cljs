(ns bali-bike.events.auth)

(defn sign-in-with-google-event
  [{:keys [db]} [_ _]]
  {:db (assoc db :signing-in? true)
   :auth/sign-in-with-google nil})

(defn auth-state-changed-event
  [{:keys [db]} [_ current-user]]
  (let [role (:role current-user)]
    {:db (assoc db
                :current-user current-user
                :signing-in? false
                :firebase-initialized? true)
     :navigation/navigate-to (if current-user
                               (if (= role "bike-owner") :bike-owner-app :app)
                               :auth)}))

(defn sign-out-event
  [_ [_ _]]
  {:auth/sign-out nil
   :navigation/navigate-to :auth})

(defn user-signed-in-event
  [_ [_ _]]
  {:notifications/request-permission nil
   :notifications/get-fcm-token nil
   :notifications/handle-initial-push-notification nil})
