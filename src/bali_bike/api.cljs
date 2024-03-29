(ns bali-bike.api
  (:require [bali-bike.http :as http]
            [bali-bike.auth :as auth]
            [re-frame.core :as rf]
            [promesa.core :as p :refer-macros [alet]]
            [bali-bike.bugsnag :as bugsnag]
            [clojure.string]))

(goog-define api-url "http://localhost:4000")
(def google-api-token "AIzaSyCJXzJMv646WQoLFYCnXWXscs33HrHAZfs")

(defn parse-query [q]
  (cond
    (keyword? q)
    (let [[query alias] (clojure.string/split (name q) #"->")]
      (if alias
        (str alias ":" query)
        query))
    (map? q)
    (str \(
         (clojure.string/join
          \,
          (map (fn [[k v]]
                 (let [v (if (keyword? v) (name v) v)]
                   (if (string? v)
                     (str (name k) ":\"" (str v) \")
                     (str (name k) ":" (str v)))))
               q))
         \))
    (vector? q)
    (str \{
         (clojure.string/join
          \space
          (map parse-query q))
         \})

    :else
    (throw (js/Error. "Cannot parse query"))))

(defn- post
  [params]
  (alet [token (p/await (auth/get-token))
         headers {"Authorization" token}
         response (p/await (http/POST api-url {:headers headers
                                               :with-credentials? false
                                               :json-params params}))]
        response))

(defn send-graphql
  [{:keys [query mutation callback-event]}]
  (let [parsed-query (if query (parse-query query) (str "mutation " (parse-query mutation)))]
    (bugsnag/leave-breadcrumb "graphql-request" {:query-name (or (first query) (first mutation))})
    (->
     (alet [response (p/await (post {:query parsed-query}))]
           (when callback-event
             (rf/dispatch [callback-event (:body response)])))
     (p/catch (fn [error]
                (if js/goog.DEBUG
                  (.log js/console (clj->js error))
                  (bugsnag/notify (clj->js error))))))))

(defn get-address-from-coordinates
  [lat lng]
  (let [url "https://maps.googleapis.com/maps/api/geocode/json"
        params {:query-params {:latlng (str lat "," lng) :key google-api-token}}]
    (->
     (alet [response (p/await (http/POST url params))]
           (:formatted_address (first (get-in response [:body :results]))))
     (p/catch (fn [error]
                (if js/goog.DEBUG
                  (.log js/console (clj->js error))
                  (bugsnag/notify (clj->js error))))))))
