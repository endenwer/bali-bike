(ns bali-bike.events.auth)

(defn sign-in-with-google-event
  [{:keys [db]} [_ _]]
  {:db (assoc db :signing-in? true)
   :auth/sign-in-with-google nil})

(defn auth-state-changed-event
  [{:keys [db]} [_ current-user]]
  (let [role (:role current-user)]
    (merge
     (if (:firebase-initialized? db) {} {:splash-screen/hide nil})
     (if current-user {:notifications/get-fcm-token nil})
     {:db (assoc db :current-user current-user :signing-in? false :firebase-initialized? true)
      :navigation/navigate-to (if current-user
                                (if (= role "bike-owner") :bike-owner-app :app) :auth)
      :notifications/handle-initial-push-notification nil})))

(defn sign-out-event
  [{:keys [db]} [_ _]]
  (let [user-uid (get-in db [:current-user :uid])
        token (:fcm-token db)]
    {:auth/sign-out nil
     :firestore/delete-fcm-token {:user-uid user-uid :token token}
     :navigation/navigate-to :auth}))

(defn user-signed-in-event
  [_ [_ _]]
  {:notifications/request-permission nil})
