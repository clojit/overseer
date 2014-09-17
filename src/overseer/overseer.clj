(ns overseer.overseer
  "A set of handlers for adding, viewing clojit-vm operate"
  (:use plumbing.core)
  (:require
   [schema.core :as s]
   [clojure.string :as str]
   [overseer.schemas :as schemas]
   [hiccup.core :as h]))

(defn constant-view [k c bcinit]
  (let [key (keyword k)]
    [:div {:style (str "background-color:#" c ";display:inline-block")}
     [:h3 k]
     [:table {:border 1}
      (map (fn [str-const i]
             [:tr [:td i] [:td str-const]])
           (key bcinit)
           (range)) ]]))


(defn rand-color []
  "#F4F3F1")

(defn render-bc [func-map]
  (map (fn [[k bc-list]]
         [:div {:style (str "display:inline-block; background-color:" (rand-color))}
          [:table
           [:tr [:th (str k)]]
           (map (fn [bc] [:tr [:td (str bc)]]) (k func-map))]])
       func-map))

(defn bc-output [bcinit]
  (h/html

   [:div {:class "bc-init"}
    (render-bc (:CFUNC (bcinit 1)) )
    (map constant-view
         ["CSTR" "CKEY" "CFLOAT" "CINT"]
         ["819FF7" "58FA58" "FE642E" "FE2E64"]
         (repeat (bcinit 1)))]))

(defnk $bcinit$POST
  "Add a new bc file"
  {:responses {200 s/Any}}
  [[:request body :- s/Any]
   [:resources bcinit index]]
  (let [entry-id (swap! index inc)
        indexed-entry (assoc body :index entry-id)]
    (swap! bcinit assoc entry-id indexed-entry)
    {:body (bc-output indexed-entry)}))

(defnk $bcinit$GET
  "View the current entries in the guestbook"
  {:responses {200 [s/Any]}}
  [[:resources bcinit]]
  {:body (bc-output @bcinit)})


