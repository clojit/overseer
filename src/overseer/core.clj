(ns overseer.core
  "This is an example project that ties together all of the pieces of fnhouse:
  1. start with a namespace ('overseer.overseer)
  2. suck up the handlers in the namespace with fnhouse.handlers library
  3. wrap each of them with schema coercion middleware from fnhouse.middleware
  4. compile all the handlers into a single router with fnhouse.routes
  5. wrap this root handler in standard ring middleware
  6. start a jetty server"
  (:use plumbing.core)
  (:require
   [ring.adapter.jetty :as jetty]
   [fnhouse.docs :as docs]
   [fnhouse.handlers :as handlers]
   [fnhouse.middleware :as middleware]
   [fnhouse.routes :as routes]
   [overseer.overseer :as overseer]
   [overseer.ring :as ring]
   [overseer.schemas :as schemas]))

(defn attach-docs [resources prefix->ns-sym]
  (let [proto-handlers (-> prefix->ns-sym
                           (assoc "docs" 'fnhouse.docs)
                           handlers/nss->proto-handlers)
        all-docs (docs/all-docs (map :info proto-handlers))]
    (-> resources
        (assoc :api-docs all-docs)
        ((handlers/curry-resources proto-handlers)))))

(defn wrapped-root-handler
  "Take the resources, partially apply them to the handlers in
  the 'overseer.overseer namespace, wrap each with a custom
  coercing middleware, and then compile them into a root handler
  that will route requests to the appropriate underlying handler.
  Then, wrap the root handler in some standard ring middleware.
  When served, the handlers will be hosted at the 'overseer' prefix."
  [resources]
  (->> (attach-docs resources {"overseer" 'overseer.overseer})
       routes/root-handler
       ring/ring-middleware))

(defn start-api
  "Take resources and server options, and spin up a server with jetty"
  [resources options]
  (-> resources
      wrapped-root-handler
      (jetty/run-jetty options)))


(defn -main
  ([]
   (-main "9090"))
  ([port]
   (start-api {:index (atom 0)
               :bcinit (atom {})}
              {:port (Integer/parseInt port) :join? false})))
