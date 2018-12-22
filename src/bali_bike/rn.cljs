(ns bali-bike.rn
  (:require [reagent.core :as r]))

(def ReactNative (js/require "react-native"))
(def ReactNativeElements (js/require "react-native-elements"))
(def MaterialIcons (js/require "react-native-vector-icons/MaterialIcons"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def scroll-view (r/adapt-react-class (.-ScrollView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def rating (r/adapt-react-class (.-Rating ReactNativeElements)))
(def material-icon (r/adapt-react-class MaterialIcons))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))
