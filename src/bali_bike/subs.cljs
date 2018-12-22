(ns bali-bike.subs
  (:require [re-frame.core :refer [reg-sub]]
            [bali-bike.db :as db]))

(reg-sub
 :bikes
 (fn [app-db _]
   (db/get-collection app-db :bikes :list)))
