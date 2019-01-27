(ns bali-bike.ui.screens.saved
  (:require [bali-bike.rn :refer [safe-area-view]]
            [bali-bike.ui.components.common :refer [h1]]
            [bali-bike.ui.components.bikes-list :as bikes-list]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/create-class
   {:component-did-mount #(rf/dispatch [:load-saved-bikes])
    :render (fn []
              [safe-area-view {:style {:flex 1 :margin 10 :margin-top 30}}
               [h1 "Saved bikes"]
               [bikes-list/main :saved-bikes]])}))
