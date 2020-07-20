(ns re-thing.persistency
  (:require
   [re-share.config.core :refer  (get!)]
   [re-share.es.common :as es :refer (day-index)]
   [rubber.template :refer (template-exists? add-template)]
   [mount.core :as mount :refer (defstate)]
   [rubber.core :refer (create bulk-create)]
   [rubber.node :as node]))

(defstate elastic
  :start (node/connect (get! :elasticsearch :default))
  :stop (node/stop))

(def types {:properties {:timestamp {:type "date" :format "epoch_millis"}
                         :host {:type "keyword"}
                         :type {:type "keyword"}}})

(defn initialize-es
  "setup Elasticsearch types and mappings for re-mote"
  []
  (es/initialize :re-thing {:readings types} true)
  (when-not (template-exists? "re-thing-readings")
    (add-template "re-thing-readings" ["re-thing*"] {:number_of_shards 1} types)))

(defn persist [m]
  (create (day-index :re-thing :readings) m))
