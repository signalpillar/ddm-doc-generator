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


(defn read-data [ & {:keys [serialized? file-path]
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

(defn prepare-adapter [adapter class-by-name]
  (let [classes (:discovered-classes adapter)]
    ;(assoc adapter :discovered-classes (map (comp :display-lable class-by-name) classes))
    classes
    ))

(defn build-adapters&jobs-doc [adapter-ids]
  (let [data (read-data :serialized? false)
        pattern-by-id (:pattern-by-id data)
        job-by-id (:job-by-id data)]
    (for [id adapter-ids]
      (let [adapter (pattern-by-id id)
            jobs (filter #(= id (:pattern-id %)) (vals job-by-id))
            adapter-doc (md-gen/generate-adapter adapter)
            jobs-doc (map md-gen/generate-job jobs)]
        [adapter-doc jobs-doc]))))

(defn build-doc [& adapter-ids]
  (let [adapter-to-jobs-pairs (build-adapters&jobs-doc adapter-ids)
        reference-doc (md-gen/generate-reference adapter-to-jobs-pairs)
        general-doc (md-gen/generate-general reference-doc)]
    general-doc))

(with-open [w (writer "document.md")]
  (binding [*out* w]
    (let [doc (build-doc "DNS_Zone")]
      (println (.toString doc)))))
