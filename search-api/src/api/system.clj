(ns api.system
  (:require [com.stuartsierra.component :as component]
            [api.components :as api-components]))

(defn system-map [config]
  (component/system-map
   :config config
   :index-service (api-components/map->Index-Service (:es-client-index config))
   :query-service (api-components/map->Query-Service (:es-client-query config))
   :api-server (api-components/map->Api-Server (:api-server config))))

(defn dependency-map [] {:api-server [:config :index-service :query-service]})

(defn create-system [& [config]]
  (component/system-using
   (system-map (or config {}))
   (dependency-map)))