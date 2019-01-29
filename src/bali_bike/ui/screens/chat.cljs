(ns bali-bike.ui.screens.chat
  (:require [bali-bike.rn :refer [gifted-chat safe-area-view]]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn render-chat []
  (r/with-let [messages (rf/subscribe [:messages])
               user (rf/subscribe [:current-user])]
    [gifted-chat {:user {:_id (:uid @user)}
                  :messages @messages}]))

(defn main []
  (r/create-class
   {:component-did-mount #(rf/dispatch [:listen-messages])
    :component-will-unmount #(rf/dispatch [:unlisten-messages])
    :render render-chat}))
