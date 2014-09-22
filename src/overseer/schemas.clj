(ns overseer.schemas
  "Schemas and constructors that are used in the overseer"
  (:require
   [clojure.string :as str]
   [schema.core :as s]))

(s/defschema Bytecode-Simple
  "A schema that maches all bytecodes"
  {(s/required-key :op) s/Keyword
   (s/optional-key :a) s/Int
   (s/optional-key :b) s/Int
   (s/optional-key :c) s/Int
   (s/optional-key :d) s/Int})

(s/defschema Bytecode-abcd
   "A schema for validation of the bytecodes"
  {(s/required-key :op) s/Keyword
   (s/required-key :a) (s/maybe s/Int)})

(s/defschema Bytecode-abc
  "A schema for validation of the bytecode B	C	A	OP"
  (merge Bytecode-abcd
         {(s/required-key :b) (s/maybe s/Int)
          (s/required-key :c) (s/maybe s/Int)}))

(s/defschema Bytecode-ad
  "A schema for validation of the bytecode D	  A	OP"
  (merge Bytecode-abcd
         {(s/required-key :d) (s/maybe s/Int)}))

(s/defschema Bytecode
  "A schema that maches all bytecodes"
  (s/either Bytecode-ad Bytecode-abc))

(s/defschema Bytecode-List
  "A schema that maches a seq of Bytecodes"
  [Bytecode])

(s/defschema Bytecode-Output-Data
  "A schema for validation of the final bytecode datastructure"
  {:CSTR [(s/maybe s/Str)]
   :CKEY [(s/maybe s/Keyword)]
   :CINT [(s/maybe s/Int)]
   :CFLOAT [(s/maybe double)]
   :CFUNC {s/Int Bytecode-List}
   })

;;-------------------------------------------------------------------------

(s/defschema Bytecode-Simple-input
  "A schema that maches all bytecodes"
  {(s/required-key "op") s/Str
   (s/optional-key "a") s/Int
   (s/optional-key "b") s/Int
   (s/optional-key "c") s/Int
   (s/optional-key "d") s/Int})

(s/defschema Bytecode-abcd-input
   "A schema for validation of the bytecodes"
  {(s/required-key "op") s/Str
   (s/required-key "a") (s/maybe s/Int)})

(s/defschema Bytecode-abc-input
  "A schema for validation of the bytecode B	C	A	OP"
  (merge Bytecode-abcd-input
         {(s/required-key "b") (s/maybe s/Int)
          (s/required-key "c") (s/maybe s/Int)}))

(s/defschema Bytecode-ad-input
  "A schema for validation of the bytecode D	  A	OP"
  (merge Bytecode-abcd-input
         {(s/required-key "d") (s/maybe s/Int)}))

(s/defschema Bytecode-input
  "A schema that maches all bytecodes"
  (s/either Bytecode-ad-input Bytecode-abc-input))

(s/defschema Bytecode-List-input
  "A schema that maches a seq of Bytecodes"
  [Bytecode-input])


(s/defschema Bytecode-Input-Data
  "A schema for validation of the final bytecode datastructure"
  {(s/required-key "CSTR") [(s/maybe s/Str)]
   (s/required-key "CKEY") [(s/maybe s/Str)]
   (s/required-key "CINT") [(s/maybe s/Int)]
   (s/required-key "CFLOAT") [(s/maybe double)]
   (s/required-key "CFUNC") {s/Str Bytecode-List-input}
   })
