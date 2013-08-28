(ns ^{:author "Vladimir Vitvitskiy"}
  ddm-doc.generatoren
  (:require [docopt.core :as docopt]
            [ddm-doc.core])
  (:gen-class))


(def USAGE "Tool to generate documentation for module and its resources

Usage:
   generatoren <path-to-class-model> <path-to-packages> <package-name>
   generatoren -h | --help


Options:
   -h, --help               Show this screen
   -u, --update-serialized  Update serialized version of data
")


(defn #^{:doc USAGE}
  -main [ & args]
  (binding [*ns* 'ddm-doc.generatoren]
    (let [arg-map (docopt/docopt args)]
      (cond
       (or (nil? arg-map) (arg-map "--help")) (println USAGE)
           (and (arg-map "<path-to-packages>")
                (arg-map "<path-to-class-model>")
                (arg-map "<package-name>"))   (println (ddm-doc.core/build-doc
                                                        (arg-map "<path-to-packages>")
                                                        (arg-map "<path-to-class-model>")
                                                        (arg-map "<package-name>")
                                                        (arg-map "--update-serialized")))
                :otherwise                    (println USAGE)))))
