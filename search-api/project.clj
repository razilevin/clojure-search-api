 (defproject search-api "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.10.0"]
                  [metosin/compojure-api "2.0.0-alpha30"]]
   :ring {:handler api.handler/app}
   :uberjar-name "server.jar"
   :profiles {:dev {:source-paths ["dev"]
                    :dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                   [ring/ring-mock "0.4.0"]
                                   [midje "1.9.9"]
                                   [clojurewerkz/elastisch "3.0.1"]
                                   [com.stuartsierra/component "0.4.0"]
                                   [ring "1.7.1"]
                                   [org.clojure/tools.logging "0.5.0"]]
                    :plugins [[lein-ring "0.12.5"]
                              [lein-midje "3.2.1"]]}})
