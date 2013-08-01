(ns ddm-doc.cits
  (:use [clojure.xml :only [parse]]
        [clojure.java.io :only [file]]
        [clojure.pprint :only [pprint]]))

(defn cit-descriptor-file? [file]
  (let [name (.getName file)
        parent (.getParentFile file)]
    (and (= "class" (.getName parent))
         (.endsWith (.toLowerCase name) ".xml"))))

(defn parse-class-definition [elm]
  (select-keys (:attrs elm) [:class-name :display-name :description]))

(defn parse-descriptor [file]
  (let [root-elm (parse file)]
    (parse-class-definition root-elm)))

(defn find-all-cits [root-path]
  (let [files (file-seq (file root-path))
        descriptors (filter cit-descriptor-file? files)]
    (pmap parse-descriptor descriptors)))
