(ns bali-bike.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [bali-bike.events]
            [bali-bike.rn :refer [app-registry]]
            [bali-bike.subs]
            [bali-bike.routing :as routing]))

(defn app-root []
  [routing/container])

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "BaliBike" #(r/reactify-component app-root)))
