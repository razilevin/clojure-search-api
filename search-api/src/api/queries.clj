(ns api.queries)

(defn simple-match-query [page items-page]
  {:query {:match {:customer_first_name "Abigail"}}
   :size items-page
   :from (* page items-page)
   :_source [:customer_first_name]})

(defn multi-match-query [page items-page]
  {:query {:multi_match {:query "Brigitte"
                         :fields [:customer_first_name :customer_last_name "customer_full_name^2"]}}
   :size items-page
   :from (* page items-page)
   :_source [:customer_full_name :customer_first_name :customer_last_name :geoip]})
