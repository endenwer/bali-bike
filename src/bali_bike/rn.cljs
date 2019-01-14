(ns bali-bike.rn
  (:require [reagent.core :as r]))

; react native

(def ReactNative (js/require "react-native"))
(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def safe-area-view (r/adapt-react-class (.-SafeAreaView ReactNative)))
(def scroll-view (r/adapt-react-class (.-ScrollView ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def activity-indicator (r/adapt-react-class (.-ActivityIndicator ReactNative)))
(defn alert [title] (.alert (.-Alert ReactNative) title))

; react native elements

(def ReactNativeElements (js/require "react-native-elements"))
(def rating (r/adapt-react-class (.-Rating ReactNativeElements)))
(def search-bar (r/adapt-react-class (.-SearchBar ReactNativeElements)))
(def list (r/adapt-react-class (.-List ReactNativeElements)))
(def list-item (r/adapt-react-class (.-ListItem ReactNativeElements)))
(def button (r/adapt-react-class (.-Button ReactNativeElements)))

; icons

(def MaterialIcons (js/require "react-native-vector-icons/MaterialIcons"))
(def material-icon (r/adapt-react-class MaterialIcons))

; firebase

(def firebase (js/require "react-native-firebase"))
(def ReactNativeGoogleSignin (js/require "react-native-google-signin"))
(def google-signin (.-GoogleSignin ReactNativeGoogleSignin))
