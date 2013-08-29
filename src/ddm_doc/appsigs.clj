(ns ddm-doc.appsigs
  (:require [clojure.java.io :as io]
            [clojure.xml :as cxml]
            [ddm-doc.xml :as xml]))

(defn- find-default-app-type [elm]
  (when-let [tag (xml/tf-> (:content elm) :Default-Application-Type)]
    (first (:content tag))))

(defn- tag? [name elm]
  (= (:tag elm) name))

(defn- filter-content-tags [name elm]
  (filter (partial tag? name) (:content elm)))

(defn- parse-app-component [default-app-type elm]
  (let [cmp (select-keys (:attrs elm)
                         [:app_id :category :ci_type :name
                          :supported_versions :vendor])
        processes (mapv :attrs (filter-content-tags :process elm))
        attributes (mapv :attrs (filter-content-tags :attribute elm))]
    (assoc cmp
      :processes processes
      :attributes attributes)))

(defn parse-sign-descriptor
  "Return sequence of components"
  [file]
  (let [elm (cxml/parse file)
        default-app-type (find-default-app-type elm)
        cmp-tags (filter-content-tags :Application-Component elm)]
    {:cmps (map (partial parse-app-component default-app-type) cmp-tags)
     :path (.getPath file)}))

(defn- parse-plugin-qualifiers [elm]
  (let [qtag (xml/tf-> (:content elm) :qualifiers)]
    (map #(vector (get-in % [:attrs :type]) (first (:content %)))
         (:content qtag ()))))

(defn- parse-plugin-dependencies [elm]
  (let [dtag (xml/tf-> (:content elm) :dependencies)]
    (map (comp first :content) (:content dtag ()))))

(defn- parse-plugin-details [elm attributes]
  (let [is-in-attributes (comp (set attributes) :tag)
        required-tags (filter is-in-attributes (:content elm))
        name-to-value (map (juxt :tag (comp first :content)) required-tags)]
    (apply hash-map (flatten name-to-value))))

(defn- parse-plugin-def [elm]
  (let [id (get-in elm [:attrs :id])
        plugin (parse-plugin-details elm [:name :description :module :class])
        qualifiers (parse-plugin-qualifiers elm)
        dependencies (parse-plugin-dependencies elm)]
    (assoc plugin
      :id id
      :qualifiers qualifiers
      :dependencies dependencies)))

(defn parse-plugin-descriptor
  "Return sequence of plugins"
  [file]
  (let [elm (cxml/parse file)
        plugins-elm (xml/tf-> (:content elm) :plugins)
        plugin-tags (filter-content-tags :plugin plugins-elm)
        plugins (mapv parse-plugin-def plugin-tags)]
    {:plugins plugins
     :path (.getPath file)}))

(defn sign-descriptor-file? [file]
  (= "applicationsSignature.xml" (.getName file)))

(defn plugin-descriptor-file? [file]
  (.endsWith (.getName file) ".package.xml"))

(defn find-all-signs [root-path]
  (let [files (file-seq (io/file root-path))
        descriptors (filter sign-descriptor-file? files)]
    (map parse-sign-descriptor descriptors)))

(defn find-all-plugins [root-path]
  (let [files (file-seq (io/file root-path))
        descriptors (filter plugin-descriptor-file? files)]
    (map parse-plugin-descriptor descriptors)))
