# Current status

This is a deep beta that have a lot of defects (no other way) but it is already can generate `.md` document based
on provided ids of adapters. There is no distributed binary yet but it can be built or ask directly me to help here (skype: vvitvitskiy).

# Roadmap

- [ ] add support for the TQL
- [ ] find out how to distribute the latest binary, jenkins/maven ?
- [ ] add multimarkdown table support

        #Tables

        column A | column B
        |:-------|:-------|
        aaa | bbb
        ccc | ddd
        eee | fff

- [ ] discuss with team how to store information that is missing right now for the complete document generation

# Usage

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
     -u, --update-serialized                   Update serialized version of data
     -pp, --packages-path                      Path to the root folder where package(s) are stored

    ```

## How to get ddm-doc tool ready to be used

`lein uberjar`

This command will include all dependencies including `Clojure` itself
