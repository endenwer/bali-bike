(ns bali-bike.ui.components.avatar
  (:require [bali-bike.rn :refer [avatar]]))

(defn main
  [{:keys [photo-url size]}]
  (if photo-url
    [avatar {:rounded true :size size :source {:uri photo-url}}]
    [avatar {:rounded true :size size :title "SL"}]))
