(ns re-thing.temp
  "Temperture sensor reading over mqtt"
  (:require
   [re-thing.spec :refer (valid?)]
   [re-thing.persistency :refer (persist)]
   [taoensso.timbre :refer (refer-timbre)]
   [re-thing.client :refer (subscribe publish log-message)]
   [cheshire.core :refer (parse-string)])
  (:import
   [com.google.json JsonSanitizer]))

(refer-timbre)

(defn scale-temp [payload]
  (update payload :temp (fn [t] (/ t 100))))

(defn temp-reading
  "Handling a temp readings"
  [message]
  (try
    (let [payload (parse-string (JsonSanitizer/sanitize (String. ^bytes (.getPayload message))) keyword)]
      (debug "persisting" payload)
      (if (valid? :re-thing.spec/reading payload)
        (case (:type payload)
          "bme280"
          (-> payload scale-temp (assoc :timestamp (System/currentTimeMillis)) persist))
        (error "payload is not valid" payload)))
    (catch Exception e
      (error "message handling failed" e))))

(defn initialize-temp []
  (subscribe "temp/reading" temp-reading))

(comment
  (initialize)
  (publish "temp/control" "readTemp"))
