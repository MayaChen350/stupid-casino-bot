(ns mayachen350.casinobot.shared
  (:require [clj-http.conn-mgr :as conn]))

(def http-client
  (conn/make-reusable-conn-manager {}))

(defn in-range [val start end]
  (and (>= val start) (< val end)))

(defn apply-if [cond func val]
  (if cond
    (func val)
    (val)))
