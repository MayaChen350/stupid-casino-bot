(ns mayachen350.casinobot.gambling.gi-guess
  (:require
   [mayachen350.casinobot.gambling.gen-data.gi-data :refer [gi-chara-data]]
   [mayachen350.casinobot.shared :refer [coll-contains?]]))

(defn count-filtered-charas [key]
  (count (filterv #(coll-contains? % key) gi-chara-data)))

