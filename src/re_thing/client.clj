(ns re-thing.client
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [mount.core :as mount :refer (defstate)]
   [dvlopt.mqtt    :as mqtt]
   [dvlopt.mqtt.v3 :as mqtt.v3]))

(refer-timbre)

(def opts
  {::mqtt/nodes  [{::mqtt/scheme :tcp
                   ::mqtt/host   "127.0.0.1"
                   ::mqtt/port   8083}]})
(defn start- []
  (::mqtt.v3/client (mqtt.v3/open opts)))

(defn stop- [c]
  (mqtt.v3/close c))

(defstate client
  :start (start-)
  :stop (stop-))

(defn on-message
  [message]
  (info "Received :" (String. ^bytes (::mqtt/payload message))))

(defn subscribe [q]
  (mqtt.v3/subscribe client {q {::mqtt/qos 1 ::mqtt.v3/on-message on-message}}))

(defn publish [q m]
  (mqtt.v3/publish client q {:mqtt/payload (.getBytes m)}))

(comment
  (subscribe "example")
  (publish "example" "hello!"))
