(ns ddm-doc.core
  (:use [clojure.pprint :only [pprint]]
        [clojure.java.io :only [file writer]])
  (:require [ddm-doc.class-model :as cm]
            [ddm-doc.jobs :as ddmjobs]
            [ddm-doc.patterns :as ddmpatterns]
            [ddm-doc.md-generator :as md-gen]))

(def ddm-root-path "/Users/signalpillar/proj/git-content-main/discovery/packages")
(def class-model-path "/Users/signalpillar/proj/git-content-main/discovery/packages/ucmdb9-ddm-layout/src/main/resources/runtime/probeManager/discoveryConfigFiles/__CmdbClassModel.bin")

(defn- group-resources [resources key-fn]
  (let [keys (map key-fn resources)]
    (zipmap keys resources)))

(defn serialize [path dict]
  (binding [*print-dup* true]
    (spit path (str dict))))

(defn read-from-file-with-trusted-contents [filename]
  (with-open [r (java.io.PushbackReader.
                 (clojure.java.io/reader filename))]
    (binding [*read-eval* true]
      (read r))))


(defn read-data
  "Read information about available adapters, jobs and class-model entries
After each call save serialized version of gathered data"
  [ & {:keys [serialized? file-path]
                     :or {serialized? false
                          file-path "serialized.txt"}}]
  (if (and serialized? (.exists (file file-path)))
    (read-from-file-with-trusted-contents file-path)
    (let [classes (:classes (cm/parse-class-model class-model-path))
          class-by-name (group-resources classes :class-name)
          jobs (ddmjobs/find-all-jobs ddm-root-path)
          job-by-id (group-resources jobs :id)
          patterns (ddmpatterns/find-all-patterns ddm-root-path)
          pattern-by-id (group-resources patterns :id)
          data {:class-by-name class-by-name
                            :job-by-id job-by-id
                            :pattern-by-id pattern-by-id}]
      (serialize file-path data)
      data)))

(defn find-entry-point-module
  "Find python entry point module in declared used-scripts section in adapter.
In case if list is indexed - script with maximum index is taken, otherwise
take the latest script in the list
@types: list -> dict
"
  [scripts]
  (if (-> scripts first :index empty?)
    (last scripts)
    (apply max-key #(Integer/parseInt (:index %)) scripts)))

(defn prepare-adapter
  "Prepare adapter for the documenting
- replace reported CITs by display labels
- replace used scripts by only one entry point module"
  [adapter class-by-name]
  (let [cits (:discovered-classes adapter)
        scripts (:used-scripts adapter)
        display-names (map (comp :display-name class-by-name) cits)]
    (assoc adapter
      :discovered-classes display-names
      :used-scripts [(find-entry-point-module scripts)])))

(defn build-adapters&jobs-doc
  "Build data ready to be included in the documentation - list of pairs
adapter to jobs declared on top of it"
  [adapter-ids]
  (let [data (read-data :serialized? false)
        class-by-name (:class-by-name data)
        pattern-by-id (:pattern-by-id data)
        job-by-id (:job-by-id data)]
    (for [id adapter-ids]
      (let [adapter (prepare-adapter (pattern-by-id id) class-by-name)
            jobs (filter #(= id (:pattern-id %)) (vals job-by-id))
            adapter-doc (md-gen/generate-adapter adapter)
            jobs-doc (map md-gen/generate-job jobs)]
        [adapter-doc jobs-doc]))))

(defn build-doc
  "Build general documentation that includes
- concepts and introductional section
- reference part
  - adapters
  - jobs"
  [adapter-ids]
  (let [adapter-to-jobs-pairs (build-adapters&jobs-doc adapter-ids)
        reference-doc (md-gen/generate-reference adapter-to-jobs-pairs)
        general-doc (md-gen/generate-general reference-doc)]
    general-doc))
