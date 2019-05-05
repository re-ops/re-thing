(ns re-thing.core
  (:require
   [mount.core :as mount :refer (defstate)])
  (:import
   [io.moquette.broker Server]
   [java.util Properties]))

(defn handler [request]
  (prn request))

(defn properties [port bind password-file]
  (let [props (Properties.)]
    (doto props
      (.setProperty "port" port)
      (.setProperty "host" bind)
      (.setProperty "password_file" password-file))))

(defn start- []
  (doto (Server.)
    (.startServer (properties "8083" "localhost" "src/main/resources/password_file.conf"))))

(defn stop- [server]
  (.stopServer server))

(defstate server
  :start (start-)
  :stop (stop- server))
