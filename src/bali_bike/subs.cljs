(ns bali-bike.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
 :bikes
 (fn [db _]
   (:bikes db)))
