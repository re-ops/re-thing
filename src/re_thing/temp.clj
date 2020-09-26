(ns re-thing.temp
  "Temperture sensor reading over mqtt"
  (:require
   [re-thing.persistency :refer (persist)]
   [taoensso.timbre :refer (refer-timbre)]
   [re-thing.client :refer (subscribe publish log-message)]
   [cheshire.core :refer (parse-string)])
  (:import
   [com.google.json JsonSanitizer]))

(refer-timbre)

(defn temp-reading
  "Handling a DHT11 temp reading"
  [message]
  (let [payload (parse-string (JsonSanitizer/sanitize (String. ^bytes (.getPayload message))) keyword)]
    (debug "persisting" payload)
    (persist (assoc payload :type :dht11 :host "" :timestamp (System/currentTimeMillis)))))

(defn initialize-temp []
  (subscribe "temp/reading" temp-reading))

(comment
  (initialize)
  (publish "temp/control" "readTemp"))
