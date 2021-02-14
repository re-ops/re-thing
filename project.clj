(defproject re-thing "0.1.0"
  :description "IoT managment Replized"
  :url "https://github.com/re-ops/re-thing"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
     [org.clojure/clojure "1.10.1"]

     ; string interpulation
     [org.clojure/core.incubator "0.1.4"]

     ; logging
     [com.taoensso/timbre "4.10.0"]
     [com.fzakaria/slf4j-timbre "0.3.19"]

     ; mqtt client
     [org.eclipse.paho/org.eclipse.paho.client.mqttv3 "1.2.5"]

     ; wiring
     [mount "0.1.13"]

     ; re-ops
     [re-share "0.16.1"]

     ; repl
     [org.clojure/tools.namespace "0.2.11"]

     ; json handling
     [cheshire "5.9.0"]
     [com.mikesamuel/json-sanitizer "1.2.0"]

     ; configuration
     [aero "1.1.6"]

     ; Elasticsearch
     [rubber "0.4.1"]
     [org.apache.httpcomponents/httpclient "4.5.2"]
   ]

   :plugins [
     [lein-cljfmt "0.5.6"]
     [lein-ancient "0.6.7" :exclusions [org.clojure/clojure]]
     [lein-tag "0.1.0"]
     [lein-set-version "0.3.0"]]

   :aliases {
     "travis" [
        "do" "clean," "compile," "cljfmt" "check"
     ]
   }

   :source-paths  ["src" "dev"]

   :resource-paths  ["src/main/resources/"]

   :repositories {
    "phao"  "https://repo.eclipse.org/content/repositories/paho-releases/"
    "sonatype" "https://oss.sonatype.org/content/repositories/releases"
    }

   :repl-options {
     :init-ns user
    :prompt (fn [ns]
              (let [hostname (.getHostName (java.net.InetAddress/getLocalHost))]
                (str "\u001B[35m[\u001B[34m" "re-thing" "\u001B[31m" "@" "\u001B[36m" hostname "\u001B[35m]\u001B[33mλ:\u001B[m ")))
    :welcome (println "Welcome to re-thing!" )
    :timeout 120000 }
)
