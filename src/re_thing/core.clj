(ns re-thing.core
  (:require
   [re-share.config.core :refer (config)]
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
    (.startServer (properties @config))))

(defn stop- [server]
  (.stopServer server))

(defstate server
  :start (start-)
  :stop (stop- server))
