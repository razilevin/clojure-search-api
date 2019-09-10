(ns api.handler
  (:require [compojure.api.sweet :refer [api context POST GET]]
            [api.queries :as es-queries]
            [ring.util.http-response :refer [ok]]
            [schema.core :as s]
            [api.es :refer [get-index create-index query-index]]))

(defn query-index-local [query-service index-name page items-page]
  (->> (es-queries/simple-match-query page items-page)
       (query-index query-service index-name page items-page)))

(defn new-handler [index-service query-service]
  (api
   {:swagger
    {:ui "/"
     :spec "/swagger.json"
     :data {:info {:title "Search API"
                   :description "Search API"
                   :basePath "/api"
                   :tags [{:name "Index", :description "Index Oriented API"}
                          {:name "Query", :description "Query Oriented API"}]}}}}
   (context "/api/index" []
     :tags ["Index"]

     (POST "/:index-name" []
       :return s/Any
       :path-params [index-name :- s/Str]
       :summary "Create Index"
       (ok (create-index index-service index-name)))

     (GET "/:index-name" []
       :return s/Any
       :path-params [index-name :- s/Str]
       :summary "Get Index Details"
       (ok (get-index index-service index-name))))

   (context "/api/query" []
     :tags ["Query"]

     (GET "/:index-name" []
       :return s/Any
       :path-params [index-name :- s/Str]
       :query-params [page :- Long items-page :- Long]
       :summary "Query Index"
       (ok (query-index-local query-service index-name page items-page))))))