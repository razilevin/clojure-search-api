(ns api.http
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(defn http-opts [body]
  (merge {:throw-exceptions false
          :accept :json
          :content-type
          :application/json
          :body (json/encode body)}))

(defn post [uri body]
  (-> uri
      (http/post (http-opts body))
      (:body)
      (json/decode true)))
