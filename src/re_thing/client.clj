(ns re-thing.client
  (:require
   [mount.core :as mount]
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

(declare subscriber)

(defn start- []
  (let [{:keys [host client-id auth]} (get! :mosquitto)
        {:keys [user password]} auth
        persistence (MemoryPersistence.)
        options (doto (MqttConnectOptions.)
                  (.setUserName user)
                  (.setPassword (.toCharArray password))
                  (.setCleanSession true))]
    (doto (MqttClient. host client-id persistence)
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

(defn subscribe [q f]
  (swap! handlers assoc q f)
  (.subscribe client (into-array String [q]) (int-array [2])))

(defn reconnect
  "re-connection loop for 1 hour in case we lost connection"
  []
  (let [i (atom 60)]
    (while (> @i 0)
      (try
        (info "trying to reconnect to mqtt server")
        (mount/stop #'client)
        (mount/start #'client)
        (info "client started")
        (doseq [[q f] @handlers]
          (subscribe q f))
        (reset! i 0)
        (catch Exception e
          (swap! i dec)
          (error "Failed to reconnect will retry again in 60 sec" e)
          (Thread/sleep (* 60 1000)))))))

(defn subscriber []
  (proxy [MqttCallback] []
    (connectionLost [throwable]
      (error "lost connection to broker" throwable)
      (reconnect))
    (messageArrived [topic message]
      ((@handlers topic) message))
    (deliveryComplete [token]
      (debug "delivered message" {:token token}))))

(defn log-message
  [message]
  (info (String. ^bytes message)))

(defn publish [q m]
  (let [message (doto (MqttMessage. (.getBytes m)) (.setQos (get! :mosquitto :qos)))]
    (.publish client q message)))
