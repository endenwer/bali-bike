(ns bali-bike.auth
  (:require-macros [promesa.core :refer [alet]])
  (:require [promesa.core :as p]
            [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(def user-instance (atom nil))

(defn get-token []
  (if-let [user @user-instance]
    (->
     (alet [token (p/await (.getIdToken user))] token)
     (p/catch (fn [error] nil)))))

(defn sign-in-with-google []
  (->
   (alet [data (p/await (.signIn rn/google-signin))
          credentials (.auth.GoogleAuthProvider.credential
                       rn/firebase (.-idToken data) (.-accessToken data))
          current-user (p/await (.signInWithCredential (.auth rn/firebase) credentials))])
   (p/catch (fn [error] (.log js/console error)))))

(defn listen-user-auth []
  (let [auth (.auth rn/firebase)]
    (.configure rn/google-signin)
    (.onAuthStateChanged auth (fn [user]
                                (reset! user-instance user)
                                (rf/dispatch [:auth-state-changed (js->clj (.toJSON user))])))))
