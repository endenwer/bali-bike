(ns bali-bike.events.bike
  (:require [bali-bike.edb :as edb]))

(defn on-bike-loaded-event
  [db [_ {:keys [data]}]]
  (edb/insert-named-item db :bikes :current (:bike data) {:loading? false}))

(defn navigate-to-bike-event
  [{:keys [db]} [_ bike-id]]
  {:db (edb/insert-named-item db :bikes :current {:id bike-id} {:loading? true})
   :api/send-graphql {:query [:bike {:id bike-id} [:id :modelId :photos :rating
                                                   :dailyPrice :monthlyPrice :reviewsCount
                                                   :mileage :manufactureYear :saved
                                                   :reviews [:id :rating :comment]
                                                   :bookings [:id :startDate :endDate]]]

                      :callback-event :on-bike-loaded}
   :navigation/navigate-to :bike})

(defn on-bikes-loaded-event
  [db [_ {:keys [data]}]]
  (let [bikes (edb/get-collection db :bikes :list)]
    (if (or
         (= 0 (count (:bikes data)))
         (and (not= 0 (count bikes)) (= (:id (last bikes)) (:id (last (:bikes data))))))
      (edb/insert-meta db :bikes :list {:loading? false :all-loaded? true})
      (edb/append-collection db :bikes :list (:bikes data) {:loading? false}))))

(defn load-bikes-event
  [{:keys [db]} [_ _]]
  (let [bikes (edb/get-collection db :bikes :list)
        bikes-meta (meta bikes)
        dates-range (:dates-range db)
        area-id (:area-filter-id db)
        model-id (:model-filter-id db)
        skip (or (:skip bikes-meta) 0)
        load-count 5]
    (when-not (or (:all-loaded? bikes-meta) (:loading? bikes-meta))
      {:db (edb/insert-meta db :bikes :list {:loading? true :skip (+ skip load-count)})
       :api/send-graphql {:query
                          [:bikes
                           (cond-> {:skip skip :first load-count}
                             area-id (merge {:areaId area-id})
                             model-id (merge {:modelId model-id})
                             dates-range (merge {:startDate (:start-date dates-range)
                                                 :endDate (:end-date dates-range)}))
                           [:id :modelId :photos
                            :dailyPrice :monthlyPrice
                            :rating :reviewsCount
                            :mileage :manufactureYear :saved]]
                          :callback-event :on-bikes-loaded}})))

(defn on-saved-bikes-loaded-event
  [db [_ {:keys [data]}]]
  (edb/insert-collection db :bikes :saved (:saved-bikes data) {:loading? false}))

(defn load-saved-bikes-event
  [{:keys [db]} [_ _]]
  {:db (edb/insert-meta db :bikes :saved {:loading? true})
   :api/send-graphql {:query [:savedBikes [:id :modelId :photos
                                           :dailyPrice :monthlyPrice
                                           :rating :reviewsCount
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
