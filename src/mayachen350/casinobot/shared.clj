(ns mayachen350.casinobot.shared)

(defn in-range [val start end]
  (and (>= val start) (< val end)))

(defn apply-if [cond func val]
  (if cond
    (func val)
    (val)))

(defn coll-contains? [coll value]
  (true? (some (partial = value) coll)))
