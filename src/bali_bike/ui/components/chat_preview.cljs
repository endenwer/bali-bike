(ns bali-bike.ui.components.chat-preview
  (:require [bali-bike.rn :as rn :refer [view touchable-highlight]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.ui.components.avatar :as avatar]
            [bali-bike.colors :as colors]
            [re-frame.core :as rf]))

(defn main
  [chat]
  [touchable-highlight {:on-press #(rf/dispatch [:navigate-to-chat (:id chat)])}
   [view {:style {:flex-direction "row"
                  :justify-content "space-between"
                  :margin-top 10}}
    [avatar/main {:photo-url (get-in chat [:another-user :photoURL])
                  :full-name (get-in chat [:another-user :name])
                  :size "medium"}]
    [view {:style {:flex 1
                   :margin-left 10
                   :padding-bottom 5
                   :border-color colors/clouds
                   :border-bottom-width 1}}
     [view {:style {:flex 1 :justify-content "space-between" :flex-direction "row"}}
      [text {:number-of-lines 1
             :style {:font-weight "bold"
                     :margin-bottom 5
                     :flex 1}}
       (get-in chat [:another-user :name])]
      [text {:style {:color colors/concrete}} (.calendar
                                               (rn/moment (:timestamp chat))
                                               nil
                                               #js {:sameDay "LT"
                                                    :lastDay "DD/MM/YYYY"
                                                    :lastWeek "DD/MM/YYYY"})]]
     [text {:number-of-lines 1 :style {:flex 1}} (:lastMessage chat)]]]])
