(ns bali-bike.api
  (:require-macros [promesa.core :refer [alet]])
  (:require [bali-bike.http :as http]
            [bali-bike.auth :as auth]
            [re-frame.core :as rf]
            [promesa.core :as p]))

(def http-url "http://localhost:4000")
(def google-api-token "AIzaSyCoOBiH4wbBS6_r43ayNXVsDOW1p5uWxpk")

(defn- post
  [params]
  (alet [token (p/await (auth/get-token))
         headers {"Authorization" token}
         response (p/await (http/POST http-url {:headers headers
                                                :with-credentials? false
                                                :json-params params}))]
        response))

(defn send-graphql
  [{:keys [query variables callback-event]}]
  (->
   (alet [response (p/await (post {:query query :variables variables}))]
         (rf/dispatch [callback-event (:body response)]))
   (p/catch (fn [error] (.log js/console (clj->js error))))))

(defn get-address-from-coordinates
  [lat lng]
  (let [url "https://maps.googleapis.com/maps/api/geocode/json"
        params {:query-params {:latlng (str lat "," lng) :key google-api-token}}]
    (->
     (alet [response (p/await (http/POST url params))]
           (:formatted_address (first (get-in response [:body :results]))))
     (p/catch (fn [error] (.log js/console error))))))
