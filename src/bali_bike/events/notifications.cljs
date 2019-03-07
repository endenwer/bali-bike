(ns bali-bike.events.notifications)

(defn set-fcm-token-event
  [{:keys [db]} [_ token]]
  {:db (assoc db :fcm-token token)
   :firestore/save-fcm-token {:user-uid (get-in db [:current-user :uid]) :token token}})

(defn notification-event-received-event
  [{:keys [db]} [_ data]]
  (.log js/console "RECEIVED"))
