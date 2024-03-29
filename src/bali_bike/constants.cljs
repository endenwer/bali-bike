(ns bali-bike.constants
  (:require [bali-bike.colors :as colors]))

(def statuses
  {"WAITING_CONFIRMATION" "WAITING CONFIRMATION"
   "CONFIRMED" "CONFIRMED"
   "CANCELED" "CANCELED"})

(def status-colors
  {"WAITING_CONFIRMATION" colors/carrot
   "CONFIRMED" colors/emerald
   "CANCELED" colors/alizarin})

(def default-region {:latitude -8.745308699651275
                     :longitude 115.16695126891136
                     :latitude-delta 0.2558138019965117
                     :longitude-delta 0.18417254090309143})
