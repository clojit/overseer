(ns overseer.core-test
  (:use plumbing.core clojure.test overseer.core)
  (:require
   [schema.core :as s]
   [clj-http.client :as client]
   [cheshire.core :as cheshire]
   [overseer.schemas :as schemas]))

(def +port+
  "A random port on which to host the service"
  6057)

(def +base-url+
  (format "http://localhost:%s/overseer/" +port+))

(defn http-post [url body]
  (client/post
   (str +base-url+ url)
   {:content-type :json
    :as :json
    :throw-exceptions false
    :body (cheshire/generate-string body)}))

(defn http-get [url & [qps]]
  (client/get
   (str +base-url+ url)
   (assoc-when
    {:as :json
     :throw-exceptions false}
    :query-params qps)))

(def server (start-api {:index (atom 0)
                        :bcinit (atom {})}
                       {:port +port+ :join? false}))

(def bc1 {:CSTR ["t" "f"],
 :CKEY [],
 :CINT [99 1 2 3],
 :CFLOAT [],
 :CFUNC
 {0
  [{:op :CFUNC, :a 0, :d 6001}
   {:op :NSSETS, :a 0, :d 0}
   {:op :CFUNC, :a 0, :d 6002}
   {:op :NSSETS, :a 0, :d 1}
   {:op :CINT, :a 0, :d 1}
   {:op :CINT, :a 1, :d 2}
   {:op :ADDVV, :a 0, :b 0, :c 1}
   {:op :CINT, :a 1, :d 3}
   {:op :ADDVV, :a 0, :b 0, :c 1}
   {:op :NSGETS, :a 2, :d 0}
   {:op :CALL, :a 2, :d 0}
   {:op :ADDVV, :a 0, :b 0, :c 1}],
  6002
  [{:op :FUNCF, :a 3, :d nil}
   {:op :CINT, :a 3, :d 1}
   {:op :MOV, :a 4, :d 0}
   {:op :DIVVV, :a 3, :b 3, :c 4}
   {:op :RET, :a 3, :d nil}],
  6001
  [{:op :FUNCV, :a 0, :d nil}
   {:op :CINT, :a 1, :d 0}
   {:op :RET, :a 1, :d nil}]}})



(clojure.pprint/pprint (http-post "bcinit" bc1))

(clojure.pprint/pprint (http-get "bcinit"))

(.stop server)
