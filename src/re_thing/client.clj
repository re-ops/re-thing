(ns re-thing.client
  (:require
   [re-share.config.core :refer  (get!)]
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
     (MqttClient. (get! :mosquitto :host) (get! :mosquitto :client-id) persistence)
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
  (swap! handlers assoc q f)
  (.subscribe client (into-array String [q]) (int-array [2])))

(defn publish [q m]
  (let [message (doto (MqttMessage. (.getBytes m)) (.setQos (get! :mosquitto :qos)))]
    (.publish client q message)))
