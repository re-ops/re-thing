(ns re-thing.config
  (:require
   [aero.core :refer (read-config)]))

(defn configuration
  ([]
   (read-config (clojure.java.io/resource "config.edn")))
  ([& ks]
   (get-in (configuration) ks)))

