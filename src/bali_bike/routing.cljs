(ns bali-bike.routing
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [clojure.string :as string]
            [reagent.core :as r]
            [bali-bike.rn :as rn]
            [bali-bike.colors :as colors]
            [bali-bike.ui.components.bike-actions-header :as bike-actions-header]
            [bali-bike.ui.screens.new-booking-map :as new-booking-map-screen]
            [bali-bike.ui.screens.booking :as booking-screen]
            [bali-bike.ui.screens.saved :as saved-screen]
            [bali-bike.ui.screens.bookings :as bookings-screen]
            [bali-bike.ui.screens.messages :as messages-screen]
            [bali-bike.ui.screens.profile :as profile-screen]
            [bali-bike.ui.screens.search :as search-screen]
            [bali-bike.ui.screens.login :as login-screen]
            [bali-bike.ui.screens.bike :as bike-screen]
            [bali-bike.ui.screens.new-booking :as new-booking-screen]
            [bali-bike.ui.screens.area-filter :as area-filter]
            [bali-bike.ui.screens.dates-filter :as dates-filter]))

(def ReactNavigation (js/require "react-navigation"))

;; navigation

(def navigation-actions (.-NavigationActions ReactNavigation))

(def navigator-ref (atom nil))

(defn navigate-to [route]
  (.dispatch
   @navigator-ref
   (.navigate
    navigation-actions
    #js {:routeName (name route)})))

(defn navigate-back []
  (.dispatch
   @navigator-ref
   (.back navigation-actions)))

;; routes

(defn- create-stack-navigator [route-configs]
  (.createStackNavigator ReactNavigation (clj->js route-configs)))

(defn- create-bottom-tab-navigator [route-configs default-navigation-options]
  (.createBottomTabNavigator
   ReactNavigation
   (clj->js route-configs)
   (clj->js default-navigation-options)))

(defn- create-switch-navigator [route-configs]
  (.createSwitchNavigator ReactNavigation (clj->js route-configs)))

(defn- create-app-container [component]
  (.createAppContainer ReactNavigation component))

(defn get-icon-name
  [tab-name]
  (case tab-name
    "search"   "search"
    "saved"    "favorite-border"
    "bookings" "event-available"
    "messages" "chat-bubble-outline"
    "profile"  "person-outline"))

(def main-tabs
  (create-bottom-tab-navigator
   {:search {:screen (r/reactify-component search-screen/main)}
    :saved {:screen (r/reactify-component saved-screen/main)}
    :bookings {:screen (r/reactify-component bookings-screen/main)}
    :messages {:screen (r/reactify-component messages-screen/main)}
    :profile {:screen (r/reactify-component profile-screen/main)}}
   {:defaultNavigationOptions
    (fn [navigator]
      (let [route-name (.-navigation.state.routeName navigator)]
        #js {:title (string/capitalize route-name)
             :tabBarIcon (fn [options]
                           (let [color (.-tintColor options)]
                             (r/create-element
                              rn/Icon
                              #js {:name (get-icon-name route-name) :color color})))}))}))
(def app-stack
  (create-stack-navigator
   {:tabs {:screen main-tabs :navigationOptions {:header nil}}
    :area-filter {:screen (r/reactify-component area-filter/main)}
    :dates-filter {:screen (r/reactify-component dates-filter/main)}
    :bike {:screen (r/reactify-component bike-screen/main)
           :navigationOptions {:headerRight (r/create-element
                                             (r/reactify-component bike-actions-header/main))}}
    :new-booking {:screen (r/reactify-component new-booking-screen/main)}
    :new-booking-map {:screen (r/reactify-component new-booking-map-screen/main)}
    :booking {:screen (r/reactify-component booking-screen/main)}}))

(defn container []
  [:> (create-app-container
       (create-switch-navigator {:auth {:screen (r/reactify-component login-screen/main)}
                                 :app {:screen app-stack}}))
   {:ref (fn [ref] (reset! navigator-ref ref))
    :persistenceKey (if js/goog.DEBUG "NavigationState" nil)}])
