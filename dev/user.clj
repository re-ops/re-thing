(ns user
  (:refer-clojure :exclude  [update list])
  (:require
   [mount.core :as mount :refer (defstate)]
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [re-thing.core :refer (server)]
   [re-thing.client :refer (client)]
   ; logging
   [re-thing.log :refer (setup-logging)]
   [re-share.log :refer (redirect-output debug-on debug-off)]
   ; testing
   [clojure.test]))


(defn start-
  "Starts the current development system."
  []
  (setup-logging)
  (mount/start #'server #'client))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (mount/stop #'server #'client))

(defn go
  "Initializes the current development system and starts it running."
  []
  (start-))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defn require-tests []
  #_(require))

(defn run-tests []
  (clojure.test/run-tests))

(defn clrs
  "clean repl"
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))
