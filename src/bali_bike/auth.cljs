(ns bali-bike.auth
  (:require [promesa.core :as p :refer-macros [alet]]
            [bali-bike.rn :as rn]
            [re-frame.core :as rf]))

(def user-instance (atom nil))

(defn get-token []
  (when-let [user @user-instance]
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
                                (alet [user-data (if user (js->clj (.toJSON user)) nil)
                                       parsed-token (p/await (.getIdTokenResult user))
                                       user-role (.-claims.role parsed-token)
                                       user-with-role (assoc user-data :role user-role)]
                                      (if js/goog.DEBUG
                                        (js/setTimeout
                                         #(rf/dispatch [:auth-state-changed user-with-role])
                                         1000)
                                        (rf/dispatch [:auth-state-changed user-with-role])))))))

(defn sign-out []
  (let [auth (.auth rn/firebase)]
    (.signOut auth)))
