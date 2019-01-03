(ns bali-bike.ui.screens.search
  (:require [bali-bike.rn :refer [text view]]
            [bali-bike.ui.components.bikes-list :as bikes-list]
            [bali-bike.ui.components.search-header :as search-header]))

(defn main []
  [view {:style {:flex 1 :flex-direction "column"}}
   [search-header/main]
   [bikes-list/main]])
