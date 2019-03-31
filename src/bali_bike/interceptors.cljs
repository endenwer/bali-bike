(ns bali-bike.interceptors
  (:require [re-frame.interceptor :refer [->interceptor get-coeffect]]
            [re-frame.utils :as re-frame-utils]
            [camel-snake-kebab.core :refer [->kebab-case-keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [bali-bike.bugsnag :as bugsnag]))

(def transform-event-to-kebab
  (->interceptor
   :id :transform-event-to-kebab
   :before (fn [context]
             (let [event (get-coeffect context :event)]
               (-> context
                   (assoc-in [:coeffects :original-event] event)
                   (assoc-in [:coeffects :event] (transform-keys ->kebab-case-keyword event)))))
   :after (fn [context]
            (-> context
                (assoc-in [:coeffects :event] (get-coeffect context :original-event))
                (re-frame-utils/dissoc-in [:coeffects :origin-event])))))

(def leave-breadcrumb
  (->interceptor
   :id :leave-breadcrumb
   :after (fn [context]
            (let [event (get-in context [:coeffects :event])]
              (bugsnag/leave-breadcrumb "re-frame-event" {:event-name (name (first event))}))
            context)))
