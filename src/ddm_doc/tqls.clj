(ns ddm-doc.tqls
  (:use [clojure.java.io :only [file]]))

(defn tql-descriptor-file? [file]
  (let [path (.getPath file)]
    (.contains path "/tql/")))

(defn- without-ext [name]
  (let [end-idx (.lastIndexOf name ".")
        idx (if (< end-idx 0) (.length name) end-idx) ]
    (.substring name 0 idx)))

(defn tql [file]
  {:name (without-ext (.getName file))
   :path (.getPath file)})

(defn find-all-tqls [root-path]
  (let [files (file-seq (file root-path))
        descriptors (filter tql-descriptor-file? files)]
    (pmap tql descriptors)))
