# overseer

A Clojure library designed to visualise some cbc (clojure bytecode)

## Usage


Start Server:
----------------

lein run


lein run *port*



Post Data:
----------------


See overseer/src/schemas.clj for input schemas

Validate rules:

(s/validate schemas/Bytecode-Input-Data json/read-str *post-input*))

curl -X POST -d  "{\"CSTR\":[\"t\",\"f\"],\"CKEY\":[\"test\",\"ckey\",\"my-keytest\"],\"CINT\":[99,1,2,3],\"CFLOAT\":[1.6,9.0,6.0,1.6,9.0],\"CFUNC\":{\"0\":[{\"op\":\"CFUNC\",\"a\":0,\"d\":6001},{\"op\":\"NSSETS\",\"a\":0,\"d\":0},{\"op\":\"CFUNC\",\"a\":0,\"d\":6002},{\"op\":\"NSGETS\",\"a\":2,\"d\":0},{\"op\":\"CALL\",\"a\":2,\"d\":0},{\"op\":\"ADDVV\",\"a\":0,\"b\":0,\"c\":1}],\"6001\":[{\"op\":\"FUNCV\",\"a\":0,\"d\":null},{\"op\":\"CINT\",\"a\":1,\"d\":0},{\"op\":\"CINT\",\"a\":3,\"d\":1},{\"op\":\"MOV\",\"a\":4,\"d\":0}],\"6002\":[{\"op\":\"FUNCF\",\"a\":3,\"d\":null},{\"op\":\"CINT\",\"a\":3,\"d\":1},{\"op\":\"MOV\",\"a\":4,\"d\":0},{\"op\":\"DIVVV\",\"a\":3,\"b\":3,\"c\":4},{\"op\":\"RET\",\"a\":3,\"d\":null}],\"6003\":[{\"op\":\"FUNCV\",\"a\":0,\"d\":null},{\"op\":\"CINT\",\"a\":1,\"d\":0},{\"op\":\"RET\",\"a\":1,\"d\":null}]}}" http://localhost:9090/overseer/bcinit/  --header "Content-Type:application/json"

Browse to:
----------------

http://localhost:9090/overseer/bcinit/


http://localhost:9090/overseer/bcinit/*index*


Problem:
----------------

The input format is strange, needs to be fixed

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
