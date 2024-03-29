(ns bali-bike.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [bali-bike.routing :as routing]
            [bali-bike.rn :as rn :refer [app-registry code-push]]
            [bali-bike.subs]
            [bali-bike.events]
            [bali-bike.auth :as auth]
            [bali-bike.notifications :as notifications]
            [bali-bike.bugsnag :as bugsnag]))

(defn app-root []
  [routing/container])

(defn init []
  (bugsnag/init)
  (bugsnag/leave-breadcrumb "init-app")
  (dispatch-sync [:initialize-db])
  (auth/listen-user-auth)
  (notifications/init)
  (.registerComponent
   app-registry
   "BaliBike"
   #(code-push (r/reactify-component app-root))))
