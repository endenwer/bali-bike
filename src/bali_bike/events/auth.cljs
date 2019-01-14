(ns bali-bike.events.auth
  (:require-macros [promesa.core :refer [alet]])
  (:require [promesa.core :as p]
            [bali-bike.rn :as rn]))

;; effects

(defn sign-in-with-google []
  (->
   (alet [data (p/await (.signIn rn/google-signin))
          credentials (.auth.GoogleAuthProvider.credential
                       rn/firebase (.-idToken data) (.-accessToken data))
          current-user (p/await (.signInWithCredential (.auth rn/firebase) credentials))])
   (p/catch (fn [error] (.log js/console error)))))

;; events

(defn sign-in-with-google-event
  [{:keys [db]} [_ _]]
  {:db (assoc db :signing-in? true)
   :auth/sign-in-with-google nil})

(defn auth-state-changed-event
  [{:keys [db]} [_ current-user]]
  {:db (assoc db :current-user current-user :signing-in? false)
   :navigation/navigate-to (if current-user :app :auth)})
