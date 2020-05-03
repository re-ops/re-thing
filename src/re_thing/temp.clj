(ns re-thing.temp
  "Temperture sensor reading over mqtt"
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [re-thing.client :refer (subscribe publish log-message)]
   [cheshire.core :refer (parse-string)]
   [dvlopt.mqtt :as mqtt])
  (:import
   [com.google.json JsonSanitizer]))

(refer-timbre)

(defn temp-reading
  "Handling a DHT11 reading"
  [message]
  (let [payload (parse-string (JsonSanitizer/sanitize (String. ^bytes (::mqtt/payload message))) keyword)]
    (info payload)))

(defn initialize []
  (subscribe "temp/reading" temp-reading))

(comment
  (initialize)
  (publish "temp/control" "readTemp"))
