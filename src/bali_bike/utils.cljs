(ns bali-bike.utils
  (:require [clojure.string :as string]))


(def moment (js/require "moment"))
(def moment-precise-range-plugin (js/require "moment-precise-range-plugin"))

(defn get-short-dates-range-string
  [start-date end-date]
  (str (.format (moment start-date) "MMM D")
       " - "
       (.format (moment end-date) "MMM D")))

(defn get-dates-diff
  [{:keys [start-date end-date]}]
  (js->clj (.preciseDiff moment (moment start-date) (moment end-date) true) :keywordize-keys true))

(defn format-number
  [number]
  (string/replace (str number) #"\B(?=(\d{3})+(?!\d))" " "))

(defn round-to-thousands
  [number]
  (* (js/Math.round (/ number 1000)) 1000))
