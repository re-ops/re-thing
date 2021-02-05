(ns re-thing.log
  (:require
   [re-share.log :as log]
   [taoensso.timbre  :as timbre :refer (merge-config! set-level! refer-timbre)]))

(refer-timbre)

(defn setup-logging
  "Sets up logging configuration:
    - stale logs removale interval
    - steam collect logs
    - log level
  "
  [& {:keys [level] :or {interval 10 level :info}}]
  (log/setup "re-thing" ["io.moquette.broker.*"] ["re-thing.output"])
  (set-level! level))

