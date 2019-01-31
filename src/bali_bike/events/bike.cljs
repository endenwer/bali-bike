(ns bali-bike.events.bike
  (:require [bali-bike.edb :as edb]))

(defn on-bikes-loaded-event
  [db [_ {:keys [data]}]]
  (edb/insert-collection db :bikes :list (:bikes data) {:loading? false}))

(defn load-bikes-event
  [{:keys [db]} [_ _]]
  {:db (edb/insert-meta db :bikes :list {:loading? true})
   :api/send-graphql {:query [:bikes [:id :modelId :photos :price :rating :reviewsCount
                                      :mileage :manufactureYear :saved]]
                      :callback-event :on-bikes-loaded}})

(defn on-saved-bikes-loaded-event
  [db [_ {:keys [data]}]]
  (edb/insert-collection db :bikes :saved (:saved-bikes data) {:loading? false}))

(defn load-saved-bikes-event
  [{:keys [db]} [_ _]]
  {:db (edb/insert-meta db :bikes :saved {:loading? true})
   :api/send-graphql {:query [:savedBikes [:id :modelId :photos :price :rating :reviewsCount
                                            :mileage :manufactureYear :saved]]
                      :callback-event :on-saved-bikes-loaded}})

(defn add-bike-to-saved-event
  [{:keys [db]} [_ id]]
  {:db (edb/prepend-collection db :bikes :saved [{:id id :saved true}])
   :api/send-graphql {:mutation [:addBikeToSaved {:bikeId id} [:userUid]]}})

(defn remove-bike-from-saved-event
  [{:keys [db]} [_ id]]
  (let [saved-bikes (edb/get-collection db :bikes :saved)
        updated-saved-bikes (filter #(not= (:id %) id) saved-bikes)]
    {:db (-> db
             (edb/insert-item :bikes {:id id :saved false})
             (edb/insert-collection :bikes :saved updated-saved-bikes))
     :api/send-graphql {:mutation [:removeBikeFromSaved {:bikeId id} [:userUid]]}}))