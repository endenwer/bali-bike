(ns bali-bike.routing
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r]
            [bali-bike.colors :as colors]
            [bali-bike.ui.screens.saved :as saved-screen]
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

(defn- create-bottom-tab-navigator [route-configs]
  (.createBottomTabNavigator ReactNavigation (clj->js route-configs)))

(defn- create-switch-navigator [route-configs]
  (.createSwitchNavigator ReactNavigation (clj->js route-configs)))

(defn- create-app-container [component]
  (.createAppContainer ReactNavigation component))

(def search-stack
  (create-stack-navigator
   {:search {:screen (r/reactify-component search-screen/main)
             :navigationOptions {:header nil}}
    :area-filter {:screen (r/reactify-component area-filter/main)
                  :navigationOptions {:headerStyle {:backgroundColor colors/clouds
                                                    :borderBottomWidth 0}}}
    :dates-filter {:screen (r/reactify-component dates-filter/main)}
    :bike {:screen (r/reactify-component bike-screen/main)
           :navigationOptions {:headerTransparent true}}
    :new-booking {:screen (r/reactify-component new-booking-screen/main)}}))

(defn- search-stack-navigation-options
  [navigator]
  (clj->js {:tabBarVisible (= (.-navigation.state.index navigator) 0)}))

(def main-tabs
  (create-bottom-tab-navigator
   {:search {:screen search-stack
             :navigationOptions search-stack-navigation-options}
    :saved {:screen (r/reactify-component saved-screen/main)}}))

(defn container []
  [:> (create-app-container
       (create-switch-navigator {:auth {:screen (r/reactify-component login-screen/main)}
                                 :app {:screen main-tabs}}))
   {:ref (fn [ref] (reset! navigator-ref ref))}])
