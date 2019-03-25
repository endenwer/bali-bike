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
  (let [diff (.preciseDiff moment (moment start-date) (moment end-date) true)
        months (.-months diff)]
    (if (> months 0)
      (js->clj diff :keywordize-keys true)
      (do
        (set! (.-days diff) (+ 1 (.-days diff)))
        (js->clj diff :keywordize-keys true)))))

(defn format-number
  [number]
  (string/replace (str number) #"\B(?=(\d{3})+(?!\d))" " "))

(defn round-to-thousands
  [number]
  (* (js/Math.round (/ number 1000)) 1000))

(defn dates-in-range
  [{:keys [start-date end-date] :as params}]
  (let [date (moment start-date)
        dates-diff (get-dates-diff params)]
    (reduce #(conj %1 (.add (.clone date) %2 "days")) [] (range (:days dates-diff)))))

(defn filter-map-by-value
  [data value]
  (if value
    (filter #(string/includes? (string/lower-case (second %)) (string/lower-case value)) data)
    data))
