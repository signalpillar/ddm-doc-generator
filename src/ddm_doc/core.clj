(ns ddm-doc.core
  (:use [clojure.pprint :only [pprint]]
        [clojure.java.io :only [file]])
  (:require [ddm-doc.class-model :as cm]
            [ddm-doc.jobs :as ddmjobs]
            [ddm-doc.patterns :as ddmpatterns]
            [ddm-doc.md-generator :as md-gen]))


(def ddm-root-path "/Users/signalpillar/proj/git-content-main/discovery/packages")
(def class-model-path "/Users/signalpillar/proj/clj/ddm-doc/test/resources/classmodel.xml")

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

(defn build-doc [& adapter-ids]
  (let [data (read-data :serialized? true)
        pattern-by-id (:pattern-by-id data)
        job-by-id (:job-by-id data)]
    (for [id adapter-ids]
      (let [adapter (pattern-by-id id)
            jobs (filter #(= id (:pattern-id %)) (vals job-by-id))]
        (md-gen/generate-doc adapter jobs)))))

(build-doc "DNS_Zone")
