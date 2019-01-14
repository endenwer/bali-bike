(ns bali-bike.ui.screens.login
  (:require [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(def ReactNativeGoogleSignin (js/require "react-native-google-signin"))
(def GoogleSigninButton (.-GoogleSigninButton ReactNativeGoogleSignin))
(def google-signin-button (r/adapt-react-class GoogleSigninButton))


(defn main []
  [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
   [google-signin-button {:on-press #(rf/dispatch [:signin-with-google])
                          :style {:width 312 :height 48}
                          :size (.-Size.Icon GoogleSigninButton)
                          :color (.-Color.Light GoogleSigninButton)}]
   [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}}
    "SETTINGS"]])
