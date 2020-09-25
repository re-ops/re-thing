(ns re-thing.client
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [mount.core :as mount :refer (defstate)])
  (:import
   org.eclipse.paho.client.mqttv3.MqttClient
   org.eclipse.paho.client.mqttv3.MqttCallback
   org.eclipse.paho.client.mqttv3.MqttConnectOptions
   org.eclipse.paho.client.mqttv3.MqttException
   org.eclipse.paho.client.mqttv3.MqttMessage
   org.eclipse.paho.client.mqttv3.persist.MemoryPersistence))

(refer-timbre)

(def qos 2)
(def broker "tcp://<ip>:1883")
(def clientId "re-thing")

(def handlers (atom {}))

(defn subscriber []
  (proxy [MqttCallback] []
    (connectionLost [throwable]
      (error "lost connection to broker" throwable))
    (messageArrived [topic message]
      ((@handlers topic) (.getPayload message)))
    (deliveryComplete [token])))

(defn start- []
  (let [persistence (MemoryPersistence.)
        options (doto (MqttConnectOptions.) (.setCleanSession true))]
    (doto
     (MqttClient. broker clientId persistence)
      (.connect options)
      (.setCallback (subscriber)))))

(defn stop- [client]
  (try
    (.disconnect client)
    (catch Exception e
      (error "failed to disconnect" e))))

(defstate client
  :start (start-)
  :stop (stop- client))

(defn log-message
  [message]
  (info (String. ^bytes message)))

(defn subscribe [q f]
  (.subscribe client (into-array String [q]) (int-array [2]))
  (swap! handlers assoc q f))

(defn publish [q m]
  (let [message (doto (MqttMessage. (.getBytes m)) (.setQos qos))]
    (.publish client q message)))
