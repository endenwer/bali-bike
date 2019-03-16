(ns bali-bike.ui.screens.login
  (:require [bali-bike.rn :refer [view safe-area-view image-background activity-indicator]]
            [bali-bike.ui.components.common :refer [text button]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(def ReactNativeGoogleSignin (js/require "react-native-google-signin"))
(def GoogleSigninButton (.-GoogleSigninButton ReactNativeGoogleSignin))
(def google-signin-button (r/adapt-react-class GoogleSigninButton))
(def loginbg-img (js/require "./images/loginbg.jpg"))


(def default-button-props
  {:raised true
   :title-style {:font-weight "600"
                 :font-size 14
                 :margin-left 12
                 :color colors/midnight-blue}
   :container-style {:margin 5}
   :icon-container-style {:margin-horizontal 6}
   :button-style {:background-color colors/white
                  :justify-content "flex-start"
                  :padding 0
                  :padding-vertical 8
                  :border-radius 2}})

(defn render-loading []
  [view {:style {:flex 1 :margin-top 30 :align-items "center" :justify-content "center"}}
   [activity-indicator {:size "large" :color colors/white}]])

(defn render-email-buttons []
  [:<>
   [button (merge default-button-props
                  {:title "Sign in with Email"
                   :icon {:name "email"}})]
   [button (merge default-button-props
                  {:title "Sign up with Email"
                   :type "outline"
                   :raised false
                   :title-style (merge (:title-style default-button-props)
                                       {:color colors/white
                                        :margin-left 0})
                   :button-style {:border-radius 2
                                  :padding-vertical 8
                                  :border-color colors/white}
                   :container-style {:margin-top 30
                                     :margin 5}})]])

(defn main []
  (r/with-let [signing-in? (rf/subscribe [:signing-in?])
               firebase-initialized? (rf/subscribe [:firebase-initialized?])]
    (if @firebase-initialized?
      [safe-area-view {:style {:align-items "center"
                               :flex 1
                               :padding 30
                               :background-color colors/emerald}}
       [view {:flex 3 :justify-content "center"}
        [text {:style {:font-size 60
                       :color colors/white
                       :font-weight "bold"}} "BaliBike"]]
       (if @signing-in?
         [render-loading]
         [view {:style {:flex 1
                        :margin-bottom 30
                        :justify-content "flex-end"}}
          [google-signin-button {:on-press #(rf/dispatch [:signin-with-google])
                                 :style {:width 312 :height 48}
                                 :size (.-Size.Wide GoogleSigninButton)
                                 :color (.-Color.Light GoogleSigninButton)}]
                                        ; TODO: implement email sign in
                                        ;[render-email-buttons]
          ])]
      [view])))
