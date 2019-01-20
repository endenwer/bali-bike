(ns bali-bike.api
  (:require-macros [promesa.core :refer [alet]])
  (:require [bali-bike.http :as http]
            [bali-bike.auth :as auth]
            [re-frame.core :as rf]
            [promesa.core :as p]))

(def http-url "http://localhost:4000")

(defn- post
  [params]
  (alet [token (p/await (auth/get-token))
         headers (if token {"Authorization" token} nil)
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
