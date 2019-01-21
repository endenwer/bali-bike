(ns bali-bike.constants
  (:require [bali-bike.colors :as colors]))

(def areas
  {:canggu "Canggu"
   :kuta "Kuta"
   :kerobokan "Kerobokan"})

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
