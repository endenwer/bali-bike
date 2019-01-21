(ns bali-bike.utils)

(def moment (js/require "moment"))

(defn get-short-dates-range-string
  [start-date end-date]
  (str (.format (moment start-date) "MMM D")
       " - "
       (.format (moment end-date) "MMM D")))
