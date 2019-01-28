(ns bali-bike.ui.components.chat-preview
  (:require [bali-bike.rn :refer [view avatar]]
            [bali-bike.ui.components.common :refer [text]]
            [bali-bike.colors :as colors]))

(defn main
  [chat]
  [view {:style {:flex-direction "row"
                 :justify-content "space-between"
                 :margin-top 10}}
   [avatar {:rounded true :medium true :title "SL"}]
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
      "Stepan Lusnikov"]
     [text {:style {:color colors/concrete}} "09:31"]]
    [text {:number-of-lines 1 :style {:flex 1}} (:last-message chat)]]])
