(ns bali-bike.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [bali-bike.rn :refer [app-registry]]
            [bali-bike.routing :as routing]
            [bali-bike.rn :as rn]
            [bali-bike.subs]
            [bali-bike.events]
            [bali-bike.auth :as auth]))

(defn app-root []
  [routing/container])

(defn init []
  (dispatch-sync [:initialize-db])
  (auth/listen-user-auth)
  (.registerComponent app-registry "BaliBike" #(r/reactify-component app-root)))
