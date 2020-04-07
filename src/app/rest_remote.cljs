(ns app.rest-remote
  (:require [com.wsscode.pathom.connect :as pc]
          [com.wsscode.pathom.core :as p]
          [com.wsscode.common.async-cljs :refer [<? <!p go-catch]]
          [com.fulcrologic.fulcro.algorithms.tx-processing :as txn]
          [cljs.core.async :refer [<! go]]
          [edn-query-language.core :as eql]
          [taoensso.timbre :as log]))

(def ingredients [{:id 1 :ingredient "Cheese" :price 0.3}
                  {:id 2 :ingredient "Salad" :price 0.2}
                  {:id 3 :ingredient "Meat" :price 1}
                  {:id 4 :ingredient "Bacon" :price 0.5}])


(pc/defresolver ingredients-resolver [env _]
  {::pc/output [{:burger/ingredients [:burger/ingredients]}]}
  {:burger/ingredients ingredients})

(def burger-builder-registry [ingredients-resolver])

(def parser
  (p/parallel-parser
    {::p/env {::p/reader [p/map-reader
                          pc/parallel-reader
                          pc/open-ident-reader
                          p/env-placeholder-reader]
              ::pc/mutation-join-globals [:tempids]
              ::p/placeholder-prefixes #{">"}}
     ::p/mutate pc/mutate-async
     ::p/plugins [(pc/connect-plugin {::pc/register burger-builder-registry})
                  p/error-handler-plugin
                  p/request-cache-plugin
                  p/trace-plugin]}))

(defn rest-remote []
  {:active-requests (atom {})
   :transmit! (fn transmit! [{:keys [active-requests]} {::txn/keys [ast result-handler update-handler] :as send-node}]
                (let [edn (eql/ast->query ast)
                      ok-handler (fn [result]
                                   (try
                                     (result-handler (select-keys result #{:transaction :status-code :body :status-text}))
                                     (catch :default e
                                       (log/error e "Result handler failed with an exception."))))
                      error-handler (fn [error-result]
                                      (try
                                        (result-handler (merge {:status-code 500} (select-keys error-result #{:transaction :status-code :body :status-text})))
                                        (catch :default e
                                          (log/error e "Error handler failed with an exception."))))]
                  (try
                    (go
                      (let [result (<? (parser {} edn))]
                        (log/debug "http call result: " result)
                        (if (contains? result ::p/errors)
                          (do
                            (log/error "http call failed:" {:transaction edn
                                                            :response (::p/errors result)})
                            (error-handler {:transaction edn :status 500}))
                          (ok-handler {:transaction edn :status-code 200 :body result}))))

                    (catch :default e
                      (error-handler {:transaction edn :status 500})))))
   :abort! (fn abort! [this id])})
