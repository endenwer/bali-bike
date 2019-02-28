(ns bali-bike.constants
  (:require [bali-bike.colors :as colors]))

(def areas
  {1 "Canggu"
   2 "Kuta"
   3 "Kerobokan"})

(def models
  {1 "Honda Vario"
   2 "Honda Scoopy"})

(def statuses
  {"WAITING_CONFIRMATION" "WAITING CONFIRMATION"
   "CONFIRMED" "CONFIRMED"
   "CANCELED" "CANCELED"})

(def status-colors
  {"WAITING_CONFIRMATION" colors/carrot
   "CONFIRMED" colors/emerald
   "CANCELED" colors/alizarin})

(def default-region {:latitude 37.78825
                     :longitude -122.4324
                     :latitude-delta 0.0922
                     :longitude-delta 0.0421})
