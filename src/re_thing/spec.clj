(ns re-thing.spec
  (:require
   [clojure.spec.alpha :as s]
   [taoensso.timbre :refer (refer-timbre)]))

(refer-timbre)

(defn valid? [s v]
  (if-not (s/valid? s v)
    (do
      (info "value is not valid" v)
      false)
    true))

(s/def ::hostname string?)

(s/def ::humidity number?)

(s/def ::preasure number?)

(s/def ::temp number?)

(s/def ::type #{"bme280"})

(s/def ::bme280
  (s/keys
   :req-un [::type ::hostname ::humidity ::preasure ::temp]))

(s/def ::reading
  (s/or
   :bme280 ::bme280))
