(defproject ddm-doc "0.0.2"
  :description "Tool to generate ddm documentation in markdown"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 ;;template engine
                 [clabango "0.5"]
                 [docopt "0.6.1"]
                 [panda-cheese "0.1.3"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :aot [ddm-doc.generatoren]
  :main ddm-doc.generatoren)
