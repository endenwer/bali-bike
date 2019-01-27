(ns bali-bike.ui.components.bike-actions-header
  (:require [bali-bike.rn :refer [view icon]]
            [bali-bike.colors :as colors]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn main []
  (r/with-let [bike-data (rf/subscribe [:current-bike])]
    (let [saved (:saved @bike-data)
          icon-name (if saved "favorite" "favorite-border")
          color (if saved colors/alizarin colors/midnight-blue)]
      [view {:style {:margin-right 10}}
       [icon {:on-press #(rf/dispatch
                          [(if saved :remove-bike-from-saved :add-bike-to-saved) (:id @bike-data)])
              :name icon-name
              :color color}]])))
