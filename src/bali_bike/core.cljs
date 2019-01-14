(ns bali-bike.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [re-graph.core :as re-graph]
            [bali-bike.rn :refer [app-registry]]
            [bali-bike.routing :as routing]
            [bali-bike.rn :as rn]
            [bali-bike.subs]
            [bali-bike.events]))

(defn listen-user-auth []
  (let [auth (.auth rn/firebase)]
    (.configure rn/google-signin)
    (.onAuthStateChanged auth (fn [user]
                                (dispatch [:auth-state-changed (js->clj (.toJSON user))])))))

(defn app-root []
  [routing/container])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch-sync [::re-graph/init {:ws-url nil :http-url "http://192.168.1.137:4000"}])
  (listen-user-auth)
  (.registerComponent app-registry "BaliBike" #(r/reactify-component app-root)))
