(ns ddm-doc.jobs
  (:use [clojure.xml :only [parse]]
        [clojure.java.io :only [file]]
        [ddm-doc.xml :only [tf->]]
        [clojure.pprint :only [pprint]]))

(defn job-descriptor-file? [file]
  (= "discoveryJobs" (.getName (.getParentFile file))))

(defn parse-parameter [elm]
  (:attrs elm))

(defn parse-parameters [elm]
  (map parse-parameter
       (-> elm :content (tf-> :parameters) :content)))

(defn parse-pattern-id [elm]
  (-> elm :content (tf-> :patternId) :content first))

(defn parse-trigger [elm]
  (-> elm :content (tf-> :triggers) :content first :content first))

(defn parse-descriptor [file]
  (let [root-elm (parse file)
        attrs (:attrs root-elm)
        display-name (:displayName attrs)
        id (:id attrs)]
    {:id id
     :display-name (if display-name display-name id)
     :description (:description attrs)
     :parameters (parse-parameters root-elm)
     :pattern-id (parse-pattern-id root-elm)
     :trigger (parse-trigger root-elm)}))

(defn find-all-jobs [root-path]
  (let [files (file-seq (file root-path))
        descriptors (filter job-descriptor-file? files)]
    (pmap parse-descriptor descriptors)))
