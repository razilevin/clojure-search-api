(ns api.components
  (:require [com.stuartsierra.component :as component]
            [clojurewerkz.elastisch.rest :as esr]
            [clojure.tools.logging :as log]
            [ring.adapter.jetty :as jetty]
            [api.handler :refer [new-handler]]))

(defrecord Index-Service [url]
  component/Lifecycle

  (start [this]
    (log/infof "Start Index-Service [%s]" (.hashCode this))
    (let [es-client (esr/connect url)]
      (assoc this :es-client es-client)))

  (stop [this]
    (log/infof "Stopped Index-Service [%s]" (.hashCode this))
    (dissoc this :es-client)))

(defrecord Query-Service [url]
  component/Lifecycle

  (start [this]
    (log/infof "Start Query-Service [%s]" (.hashCode this))
    (let [es-client (esr/connect url)]
      (assoc this :es-client es-client)))

  (stop [this]
    (log/infof "Stopped Query-Service [%s]" (.hashCode this))
    (dissoc this :es-client)))

(defn run-api-server [port handler]
  (jetty/run-jetty handler {:port port :join? false}))

(defrecord Api-Server [index-service query-service config]
  component/Lifecycle

  (start [this]
    (log/infof "Start Api-Server [%s]" (.hashCode this))
    (let [{:keys [port]} config
          handler (new-handler index-service query-service)
          server (run-api-server port handler)]
      (assoc this :server server)))

  (stop [this]
    (log/infof "Stopped Api-Server [%s]" (.hashCode this))
    (.stop (:server this))
    (dissoc this :server)))
