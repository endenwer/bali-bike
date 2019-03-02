(ns bali-bike.ui.components.avatar
  (:require [bali-bike.rn :refer [avatar]]
            [clojure.string :as string]))

(defn main
  [{:keys [full-name photo-url size]}]
  (let [initials (string/upper-case (string/join (map first (string/split full-name " "))))]
    (if photo-url
      [avatar {:rounded true :size size :source {:uri photo-url}}]
      [avatar {:rounded true :size size :title initials}])))
