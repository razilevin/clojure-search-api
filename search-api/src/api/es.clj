(ns api.es
  (:require [clojurewerkz.elastisch.rest.index :as idx]
            [clojurewerkz.elastisch.rest :as rest]
            [api.http :refer [post]]))

(defn create-index [index-service index-name]
  {})

(defn get-index [index-service index-name]
  (let [{:keys [es-client]} index-service
        fns [idx/get-settings idx/get-mapping]
        index-name-key (keyword index-name)]
    (reduce conj {:stats (idx/stats es-client index-name)}
            (map #(index-name-key (% es-client index-name)) fns))))

(defn extract-paging [reply page]
  {:total (get-in reply [:hits :total :value])
   :page page})

(defn flatten-map
  ([kvs lvl]
   (mapcat (fn [[k v]]
             (if (and (map? v) (>= lvl 1))
               (flatten-map v (dec lvl))
               [[k v]])) kvs)))

(defn flatten-select-keys [m & keys]
  (flatten-map (select-keys m keys) 1))

(defn reduce-to-map [xs]
  (reduce #(assoc %1 (keyword (first %2)) (second %2)) {} xs))

(def to-map
  (comp reduce-to-map #(flatten-select-keys % :_source :_score :_id)))

(defn extract-data [reply]
  (as-> reply r
    (get-in r (repeat 2 :hits))
    (map to-map r)))

(defn query-index [query-service index-name page items-page query]
  (let [{:keys [es-client]} query-service
        reply (post (rest/search-url es-client index-name) query)]
    (def x reply)
    (-> {}
        (assoc :data (extract-data reply))
        (assoc :paging (extract-paging reply page)))))