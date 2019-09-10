(ns user
  (:require [api.system :as system]
            [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :refer (refresh refresh-all)]
            [clojure.pprint :refer (pprint)]
            [cheshire.core :as json]))

(def system-config
  {:es-client-index {:url "http://es01:9200"}
   :es-client-query {:url "http://es02:9200"}
   :port 8000})

(def the-system nil)

(defn init []
  (alter-var-root #'the-system
                  (constantly (system/create-system system-config))))

(defn start []
  (alter-var-root #'the-system component/start))

(defn stop []
  (alter-var-root #'the-system
                  #(when % (component/stop %))))

(defn go []
  (init)
  (start))