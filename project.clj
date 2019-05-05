(defproject re-thing "0.1.0"
  :description "IoT managment Replized"
  :url "https://github.com/re-ops/re-thing"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
     [org.clojure/clojure "1.10.0"]

     ; string interpulation
     [org.clojure/core.incubator "0.1.4"]

     ; logging
     [com.taoensso/timbre "4.10.0"]
     [com.fzakaria/slf4j-timbre "0.3.7"]
     [com.taoensso/tufte "1.1.1"]

     ; mqtt server
     [io.moquette/moquette-broker "0.12.1"]

     ; mqtt client
     [dvlopt/mqtt "0.0.1"]

     ; wiring
     [mount "0.1.13"]

     ; re-ops
     [re-share "0.10.1"]

     ; repl
     [org.clojure/tools.namespace "0.2.11"]
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

   :repositories {"bintray" "https://jcenter.bintray.com"}
)
