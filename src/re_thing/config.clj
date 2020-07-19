(ns re-thing.config
  (:require
   [re-share.config.core :refer (config)]
   [aero.core :refer (read-config)]))

(defn initialize-config
  []
  (reset! config (read-config (clojure.java.io/resource "config.edn"))))
