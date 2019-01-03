(ns bali-bike.routing
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r]
            [bali-bike.ui.screens.saved :as saved-screen]
            [bali-bike.ui.screens.search :as search-screen]
            [bali-bike.ui.screens.bike :as bike-screen]
            [bali-bike.ui.screens.area-filter :as area-filter]))

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

(defn- create-app-container [component]
  (.createAppContainer ReactNavigation component))

(def search-stack
  (create-stack-navigator
   {:search {:screen (r/reactify-component search-screen/main)
             :navigationOptions {:header nil}}
    :area-filter {:screen (r/reactify-component area-filter/main)}
    :bike {:screen (r/reactify-component bike-screen/main)}}))

(defn- search-stack-navigation-options
  [navigator]
  (clj->js {:tabBarVisible (= (.-navigation.state.index navigator) 0)}))

(defn container []
  [:> (create-app-container (create-bottom-tab-navigator
                             {:search {:screen search-stack
                                       :navigationOptions search-stack-navigation-options}
                              :saved {:screen (r/reactify-component saved-screen/main)}}))
   {:ref (fn [ref] (reset! navigator-ref ref))}])
