(defproject ddm-doc "0.0.2"
  :description "Tool to generate ddm documentation in markdown"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [fleet "0.10.1"]
                 [org.clojure/tools.cli "0.2.2"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :main ddm-doc.generatoren)
