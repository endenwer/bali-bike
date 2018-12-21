(ns bali-bike.routing
  (:require [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [reagent.core :as r]
            [bali-bike.ui.screens.saved :as saved-screen]
            [bali-bike.ui.screens.search :as search-screen]))

(def ReactNavigation (js/require "react-navigation"))

(defn create-bottom-tab-navigator [route-configs]
  (.createBottomTabNavigator ReactNavigation (clj->js route-configs)))

(defn create-app-container [component]
  (.createAppContainer ReactNavigation component))

(defn container []
  [:> (create-app-container (create-bottom-tab-navigator
                             {:Search {:screen (r/reactify-component search-screen/main)}
                              :Saved {:screen (r/reactify-component saved-screen/main)}}))])
