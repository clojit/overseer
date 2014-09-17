(ns overseer.overseer
  "A set of handlers for adding, viewing clojit-vm operate"
  (:use plumbing.core)
  (:require
   [schema.core :as s]
   [clojure.string :as str]
   [overseer.schemas :as schemas]
   [hiccup.core :as h]))

(def const-table-color {"CSTR" "819FF7"
                        "CKEY" "58FA58"
                        "CFLOAT" "FE642E"
                        "CINT"  "FE2E64"})

(def t-width 300)

(defn constant-view [k c bcinit]
  (let [key (keyword k)]
    [:tr
     [:td {:style (str "background-color:#" c ";display:inline-block")}
         [:h4 k]
         [:table {:width t-width :border 1}
          (map (fn [str-const i]
                 [:tr [:td i] [:td str-const]])
               (key bcinit)
               (range))]]]))

(defn rand-color []
  (let [digit (vec  (concat (map str (range 10)) ["A" "B" "C" "D" "E" "F"]))]
    (str "#"
         (apply str (take 6 (repeatedly #(rand-nth digit)))))))


(defn add-bg-for-consttable [html-element bc]
    (if (some #{(str (:op bc))} (vec (keys const-table-color)))
          [:span {:style (str "background-color:#" (get const-table-color (str (:op bc))) ";")} html-element]
          html-element))

(defn add-link-for-fn [html-element bc]
  (if (= (:op bc) "CFUNC")
    [:a {:href (str "#:"  (:d bc))} html-element]
    html-element))

(defn render-bc [bc]
  [:p "{:op " (:op bc)
       " :a " (:a bc)
       " :d " (add-link-for-fn (add-bg-for-consttable (str (:d bc)) bc) bc) "}" ])


(defn render-bc-list [func-map]
  (map (fn [[k bc-list]]
         [:tr
          [:td {:style (str "display:inline-block;background-color:" (rand-color))}
           [:table
            [:tr [:th {:id (str k)} (str k)]]
            (map (fn [bc] [:tr
                           [:td (render-bc bc)]])
                 (k func-map))]]])
       func-map))

(defn bc-output [bcinit]
  (h/html
   [:div {:float "left"}
    [:code
     [:table  {:border 1}
      (render-bc-list (:CFUNC (bcinit 1)))]]]
   [:div {:style (str "position:fixed;top:1em;right:1em;")}
    [:table {:width t-width :border 1}
     [:code
      (map constant-view
           ["CSTR" "CKEY" "CFLOAT" "CINT"]
           ["819FF7" "58FA58" "FE642E" "FE2E64"]
           (repeat (bcinit 1)))]]]))

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


