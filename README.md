# ddm-doc

Tool to generate documentation for adapter and related jobs
Example,
`java -jar ddm-doc.jar -cm <path-to-class-model> -pp <path-to-packages> <adapter-id1> <adapter-id2> > doc.md`

## How to get ddm-doc tool ready to be used

`lein uberjar`

This command will include all dependencies including `Clojure` itself


