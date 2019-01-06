(ns bali-bike.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [re-graph.core :as re-graph]
            [bali-bike.rn :refer [app-registry]]
            [bali-bike.routing :as routing]
            [bali-bike.subs]
            [bali-bike.events]))

(defn app-root []
  [routing/container])

(defn init []
  (dispatch-sync [:initialize-db])
  (dispatch-sync [::re-graph/init {:ws-url nil
                                   :http-url "http://localhost:4000"}])
  (.registerComponent app-registry "BaliBike" #(r/reactify-component app-root)))
