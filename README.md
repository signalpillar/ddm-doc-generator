# ddm-doc

[Current version of tool, already built](ddm-doc.0.0.1.jar)

    ```

    Tool to generate documentation for adapter and related jobs
    Example,
    java -jar ddm-doc.jar -cm <path-to-class-model> -pp <path-to-packages> <adapter-id1> <adapter-id2> > doc.md

    Usage:

     Switches                         Default  Desc
     --------                         -------  ----
     -h, --no-help, --help            false    Print this help message
     -j, --no-jobs-only, --jobs-only  false    Generate documentation for the jobs only belonging to the adapters
     -cm, --classmodel-path                    Path to the .bin file of the class model
     -pp, --packages-path                      Path to the root folder where package(s) are stored

    ```

## How to get ddm-doc tool ready to be used

`lein uberjar`

This command will include all dependencies including `Clojure` itself
