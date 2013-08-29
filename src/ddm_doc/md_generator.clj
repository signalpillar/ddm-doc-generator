(ns ddm-doc.md-generator
  (:require [clabango.parser :refer [render-file]]))

(defn- ci-data-source [data]
  (if-let [dot-idx (.indexOf (:value data) ".")]
     (.substring (:value data) 0 dot-idx)
     (:value data)))

(defn generate-adapter [adapter]
  (let [classes (sort (:discovered-classes adapter))
        tcd (sort-by ci-data-source (:triggered-ci-data adapter))
        adapter (assoc adapter
                  :discovered-classes classes
                  :triggered-ci-data tcd
                  :has-parameters (> (count (:parameters adapter)) 0))]
    (render-file "resources/adapter.md" adapter)))

(defn generate-job [job]
  (render-file "resources/job.md"
               (assoc job :has-parameters (> (count (:parameters job)) 0))))

(defn generate-reference [pattern-to-jobs-pairs cmp-to-plugins]
  (render-file "resources/reference.md"
               {:patterns (map first pattern-to-jobs-pairs)
                :jobs (mapcat second pattern-to-jobs-pairs)}))

(defn generate-general [reference]
  (render-file "resources/general.md" {:content (str reference)}))

(defn generate-signs [plugins-by-sign]
  "Generate reference information about application components and plugins"
  (render-file "resources/appsign.md" {:plugins-by-sign plugins-by-sign}))
