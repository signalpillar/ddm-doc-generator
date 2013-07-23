(ns ddm-doc.patterns
  (:use [clojure.xml :only [parse]]
        [ddm-doc.xml :only [tf->]]
        [clojure.java.io :only [file]]
        [clojure.pprint :only [pprint]]))

(defn pattern-descriptor-file? [file]
  (= "discoveryPatterns" (.getName (.getParentFile file))))

(defn parse-parameter [elm]
  (:attrs elm))

(defn parse-parameters [elm]
  (map parse-parameter
       (-> elm :content (tf-> :parameters) :content)))

(defn parse-nested-content [elm]
  (-> elm :content first))

(defn parse-nested-elements [elm element-name parse-fn]
  (map parse-fn (-> elm :content (tf-> element-name) :content)))

(defn parse-discovered-classes [elm]
  (parse-nested-elements elm :discoveredClasses parse-nested-content))

(defn parse-protocols [elm]
  (parse-nested-elements elm :protocols parse-nested-content))

(defn parse-used-scripts [elm])

(defn parse-descriptor [file]
  (let [root-elm (parse file)
        attrs (:attrs root-elm)]
    {:id (:id attrs)
     :description (:description attrs)
     :display-name (:displayName attrs)
     :used-scripts (parse-used-scripts root-elm)
     :global-configuration-files []
     :parameters (parse-parameters root-elm)
     :discovered-classes (parse-discovered-classes root-elm)
     :protocols (parse-protocols root-elm)
     :input-cit (first (parse-nested-elements root-elm :inputClass identity))}))

(defn find-all-patterns [root-path]
  (let [files (file-seq (file root-path))
        descriptors (filter pattern-descriptor-file? files)]
    (pmap parse-descriptor descriptors)))

;(find-all-patterns "/Users/signalpillar/proj/git-content-main/discovery/packages/database_basic/")
