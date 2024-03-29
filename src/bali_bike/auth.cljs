(ns bali-bike.auth
  (:require [promesa.core :as p :refer-macros [alet]]
            [bali-bike.rn :as rn]
            [re-frame.core :as rf]
            [bali-bike.bugsnag :as bugsnag]))

(def user-instance (atom nil))

(defn get-token []
  (if-let [user @user-instance]
    (->
     (alet [token (p/await (.getIdToken user))] token)
     (p/catch (fn [error] nil)))
    (p/promise nil)))

(defn sign-in-with-google []
  (->
   (alet [data (p/await (.signIn rn/google-signin))
          credentials (.auth.GoogleAuthProvider.credential
                       rn/firebase (.-idToken data) (.-accessToken data))
          current-user (p/await (.signInWithCredential (.auth rn/firebase) credentials))]
         (rf/dispatch [:user-signed-in]))
   (p/catch (fn [error]
              (if js/goog.DEBUG
                (.log js/console (clj->js error))
                (bugsnag/notify (clj->js error)))))))

(defn- auth-state-changed
  [user]
  (if js/goog.DEBUG
    (js/setTimeout
     #(rf/dispatch [:auth-state-changed user])
     1000)
    (rf/dispatch [:auth-state-changed user])))

(defn listen-user-auth []
  (bugsnag/leave-breadcrumb "init-user-listener")
  (let [auth (.auth rn/firebase)]
    (.configure rn/google-signin)
    (.onAuthStateChanged auth (fn [user]
                                (reset! user-instance user)
                                (if user
                                  (do
                                    (bugsnag/leave-breadcrumb "auth-state-changed-nil")
                                    (alet [user-data (js->clj (.toJSON user))
                                           parsed-token (p/await (.getIdTokenResult user))
                                           user-role (.-claims.role parsed-token)
                                           user-with-role (assoc user-data :role user-role)]
                                          (auth-state-changed user-with-role)))
                                  (do
                                    (bugsnag/leave-breadcrumb "auht-state-changed")
                                    (auth-state-changed nil)))))))

(defn sign-out []
  (let [auth (.auth rn/firebase)]
    (.signOut auth)))
