(ns bali-bike.ui.screens.search
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [bali-bike.rn :refer [view]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.bikes-list :as bikes-list]
            [bali-bike.ui.components.search-header :as search-header]))

(defn main []
  (r/create-class
   {:component-did-mount (fn [] (rf/dispatch [:load-bikes]))
    :render (fn []
              [view {:style {:flex 1 :flex-direction "column"}}
               [search-header/main]
               [bikes-list/main]])}))
