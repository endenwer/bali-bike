(ns bali-bike.edb
  (:require [clojure.spec.alpha :as s]
            [entitydb.core :as edb]))

(def dbal (edb/make-dbal {:bikes {:id :id
                                  :relations {:reviews [:many :reviews]
                                              :bookings [:many :bookings]}}
                          :reviews {:id :id
                                    :relations {:bike [:one :bikes]}}
                          :bookings {:id :id
                                     :relations {:bike [:one :bikes]}}
                          :chats {:id :id}
                          :messages {:id :id}}))

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
  ([db entity-kw id data] (update-named-item db entity-kw id data nil))
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
(def initial-app-db {})
