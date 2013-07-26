(ns ^{:author "Vladimir Vitvitskiy"}
  ddm-doc.generatoren
  (:require [clojure.tools.cli :as cli]
            [ddm-doc.core])
  (:gen-class))

(def HEADER "Tool to generate documentation for adapter and related jobs
Example,
java -jar ddm-doc.jar -cm <path-to-class-model> -pp <path-to-packages> <adapter-id1> <adapter-id2> > doc.md
")

(defn- add-header [banner]
  (format "%s
%s
" HEADER banner))

(defn- error [banner & msgs]
  (do
    (println (apply str msgs))
    (println (add-header banner))
    (System/exit 1)))

(defn- help [banner]
  (do
    (println (add-header banner))
    (System/exit 0)))

(defn -main [ & arguments]
  (let [[options args banner] (cli/cli arguments
                                       ["-h" "--help" "Print this help message" :flag true]
                                       ["-j" "--jobs-only" "Generate documentation for the jobs only belonging to the adapters" :flag true]
                                       ["-cm" "--classmodel-path" "Path to the .bin file of the class model"]
                                       ["-u" "--update-serialized" "Update serialized version of data" :flag false]
                                       ["-pp" "--packages-path" "Path to the root folder where package(s) are stored"]
                                       )
        adapter-ids args]
    (cond
     (:help options) (help)
     (not (seq adapter-ids)) (error banner "Adapter IDs are not specified")
     (not (:packages-path options)) (error banner "Path to the packages is not specified")
     (not (:classmodel-path options)) (error banner "Path to the class model file is not specified"))
    (println  (str (ddm-doc.core/build-doc
                    (:packages-path options)
                    (:classmodel-path options)
                    adapter-ids (:update-serialized options))))))
