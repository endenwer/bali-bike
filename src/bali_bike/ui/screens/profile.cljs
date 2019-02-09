(ns bali-bike.ui.screens.profile
  (:require [bali-bike.rn :refer [safe-area-view view list-item]]
            [bali-bike.ui.components.common :refer [h1 h3 text]]
            [bali-bike.ui.components.avatar :as avatar]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [bali-bike.colors :as colors]))

(defn render-user
  [{:keys [display-name photo-url]}]
  [view {:flex-direction "row" :margin-vertical 30 :align-items "center"}
   [avatar/main {:photo-url photo-url :size "large"}]
   [h3 {:margin-left 10} display-name]])

(defn render-actions []
  [view {:style {:border-top-width 1 :border-color colors/clouds}}
   [list-item {:title "Sign out"
               :on-press #(rf/dispatch [:sign-out])
               :container-style {:border-bottom-width 1
                                 :border-color colors/clouds}}]])

(defn main []
  (r/with-let [current-user (rf/subscribe [:current-user])]
    [safe-area-view {:style {:flex 1 :margin 10 :margin-top 30}}
     [h1 "Profile"]
     [render-user @current-user]
     [render-actions]]))
