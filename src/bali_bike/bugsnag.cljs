(ns bali-bike.bugsnag
  (:require [bali-bike.rn :as rn]))

(def client (atom nil))

(defn notify
  [data]
  (.notify @client data))

(defn init []
  (reset! client (rn/bugsnag-client. "50eb996dbf780a21d7856daf3d9ed44e")))
