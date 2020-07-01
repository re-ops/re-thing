(ns re-thing.core
  (:require
   [re-thing.config :refer (configuration)]
   [mount.core :as mount :refer (defstate)])
  (:import
   [io.moquette.broker Server]
   [java.util Properties]))

(defn handler [request]
  (prn request))

(defn properties [{:keys [port bind password-file]}]
  (let [props (Properties.)]
    (doto props
      (.setProperty "port" port)
      (.setProperty "host" bind)
      (.setProperty "password_file" password-file))))

(defn start- []
  (doto (Server.)
    (.startServer (properties (configuration)))))

(defn stop- [server]
  (.stopServer server))

(defstate server
  :start (start-)
  :stop (stop- server))
