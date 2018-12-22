(ns bali-bike.db
  (:require [clojure.spec.alpha :as s]
            [entitydb.core :as edb]))

(def dbal (edb/make-dbal {:bikes {:id :id}}))

(defn insert-item [& args] (apply (:insert-item dbal) args))
(defn insert-named-item [& args] (apply (:insert-named-item dbal) args))
(defn insert-collection [& args] (apply (:insert-collection dbal) args))
(defn append-collection [& args] (apply (:append-collection dbal) args))
(defn prepend-collection [& args] (apply (:prepend-collection dbal) args))
(defn insert-meta [& args] (apply (:insert-meta dbal) args))
(defn remove-item [& args] (apply (:remove-item dbal) args))
(defn remove-named-item [& args]  (apply (:remove-named-item dbal) args))
(defn remove-collection [& args] (apply (:remove-collection dbal) args))
(defn remove-meta [& args] (apply (:remove-meta dbal) args))
(defn get-item-by-id [& args]  (apply (:get-item-by-id dbal) args))
(defn get-named-item [& args]  (apply (:get-named-item dbal) args))
(defn get-collection [& args] (apply (:get-collection dbal) args))
(defn get-item-meta [& args]  (apply (:get-item-meta dbal) args))
(defn get-named-item-meta [& args]  (apply (:get-named-item-meta dbal) args))
(defn get-collection-meta[& args]  (apply (:get-collection-meta dbal) args))
(defn vacuum [& args] (apply (:vacuum dbal) args))

(defn update-item-by-id
  ([db entity-kw id data] (update-item-by-id db entity-kw id data nil))
  ([db entity-kw id data meta]
   (let [item (get-item-by-id db entity-kw id)]
     (insert-item db entity-kw (merge item data) meta))))

(defn update-named-item
  ([db entity-kw id data] (update-item-by-id db entity-kw id data nil))
  ([db entity-kw id data meta]
   (let [item (get-named-item db entity-kw id)]
     (insert-item db entity-kw (merge item data) meta))))

(defn update-meta
  [db entry-kw id meta]
  (let [item-meta (get-item-meta entry-kw id)]
    (insert-meta db entry-kw (merge item-meta meta))))

(defn get-collection-items-meta
  [db entity-kw collection-key]
  (let [collection (get-collection db entity-kw collection-key)]
    (map #(get-item-meta db entity-kw (:id %)) collection)))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::greeting]))

;; initial state of app-db
(def app-db (insert-collection {} :bikes :list  [{:id 1
                                                  :name "Honda Vario"
                                                  :rating 4.3
                                                  :reviews-count 23
                                                  :price 750
                                                  :photos ["https://gitlab.pro/yuji/demo/uploads/d6133098b53fe1a5f3c5c00cf3c2d670/DVrj5Hz.jpg_1"
                                                           "https://gitlab.pro/yuji/demo/uploads/2d5122a2504e5cbdf01f4fcf85f2594b/Mwb8VWH.jpg"
                                                           "https://gitlab.pro/yuji/demo/uploads/4421f77012d43a0b4e7cfbe1144aac7c/XFVzKhq.jpg"
                                                           "https://gitlab.pro/yuji/demo/uploads/576ef91941b0bda5761dde6914dae9f0/kD3eeHe.jpg"]}
                                                 {:id 2
                                                  :name "Honda Scoopy"
                                                  :rating 4.7
                                                  :reviews-count 11
                                                  :price 700
                                                  :photos ["https://gitlab.pro/yuji/demo/uploads/d6133098b53fe1a5f3c5c00cf3c2d670/DVrj5Hz.jpg_1"
                                                           "https://gitlab.pro/yuji/demo/uploads/2d5122a2504e5cbdf01f4fcf85f2594b/Mwb8VWH.jpg"
                                                           "https://gitlab.pro/yuji/demo/uploads/4421f77012d43a0b4e7cfbe1144aac7c/XFVzKhq.jpg"
                                                           "https://gitlab.pro/yuji/demo/uploads/576ef91941b0bda5761dde6914dae9f0/kD3eeHe.jpg"]}]))
