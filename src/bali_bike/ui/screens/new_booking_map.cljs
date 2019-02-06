(ns bali-bike.ui.screens.new-booking-map
  (:require [bali-bike.rn :refer [view icon safe-area-view]]
            [bali-bike.ui.components.common :refer [button]]
            [bali-bike.ui.components.map-view :as map-view]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn render-bottom
  [{:keys [on-save loading?]}]
  [safe-area-view {:style {:padding 10 :align-self "stretch"}}
   [button {:title "Save"
            :loading loading?
            :disabled loading?
            :on-press on-save
            :background-color colors/turquoise
            :container-view-style {:margin-left 10
                                   :margin-right 10
                                   :margin 10}}]])

(defn main []
  (r/with-let [delivery-location (rf/subscribe [:delivery-location])
               change-region #(rf/dispatch [:update-delivery-region (js->clj %)])]
    [view {:style {:flex 1}}
     [map-view/main {:on-change change-region
                     :initial-region (:region @delivery-location)}]
     [render-bottom
      {:loading? (:loading? @delivery-location)
       :on-save
       #(rf/dispatch [:set-delivery-location])}]]))
