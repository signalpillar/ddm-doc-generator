(ns ddm-doc.core
  (:use [clojure.pprint :only [pprint]]
        [clojure.java.io :only [file writer]]
        [panda-cheese.core :only [generate-class-diagram]])
  (:require [ddm-doc.class-model :as cm]
            [ddm-doc.cits :as ddmcits]
            [ddm-doc.jobs :as ddmjobs]
            [ddm-doc.tqls :as ddmtqls]
            [ddm-doc.patterns :as ddmpatterns]
            [ddm-doc.md-generator :as md-gen]))

(defn- group-resources [resources key-fn]
  (let [keys (map key-fn resources)]
    (zipmap keys resources)))

(defn serialize [path dict]
  (spit path (str dict)))

(defn read-from-file-with-trusted-contents [filename]
  (with-open [r (java.io.PushbackReader.
                 (clojure.java.io/reader filename))]
    (binding [*read-eval* true]
      (read r))))


(defn read-data
  "Read information about available adapters, jobs and class-model entries
After each call save serialized version of gathered data"
  [ddm-root-path class-model-path
   & {:keys [update-serialized? file-path]
                     :or {update-serialized? false
                          file-path "serialized.txt"}}]
  (if (and (not update-serialized?) (.exists (file file-path)))
    (read-from-file-with-trusted-contents file-path)
    (let [class-by-name (merge
                   (group-resources
                    (cm/parse-class-model class-model-path) :class-name)
                   (group-resources
                    (ddmcits/find-all-cits ddm-root-path) :class-name))
          tqls (ddmtqls/find-all-tqls ddm-root-path)
          tql-by-name (group-resources tqls :name)
          jobs (ddmjobs/find-all-jobs ddm-root-path)
          job-by-id (group-resources jobs :id)
          adapters (ddmpatterns/find-all-patterns ddm-root-path)
          data {:class-by-name class-by-name
                :tql-by-name tql-by-name
                :job-by-id job-by-id
                :adapters adapters}]
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



(defn generate-trigger-tql [tql]
  (let [path (file (:path tql))
        name (.getName path)]
    (generate-class-diagram path)
    {:file-path (format "%s.png" name)
     :file-name name}))

(defn prepare-job
  [job tql-by-name]
  (if-let [tqls (seq (map tql-by-name (:triggers job)))]
    (assoc job :trigger-tqls (map generate-trigger-tql tqls))
    job))

(defn get-cit-display-name [class-by-name name]
  (if-let [cls (class-by-name name)]
    (:display-name cls)
    (throw (IllegalArgumentException. (str "Missed display name for the " name)))))

(defn prepare-adapter
  "Prepare adapter for the documenting
- replace reported CITs by display labels
- replace used scripts by only one entry point module"
  [adapter class-by-name]
  (let [cits (:discovered-classes adapter)
        scripts (:used-scripts adapter)
        display-names (map (partial get-cit-display-name class-by-name) cits)
        input-cit (get-cit-display-name class-by-name (:input-cit adapter))
        tql-img-path (format "%s_input_tql.png" (:id adapter))]
    (generate-class-diagram (:input-tql-root-elm adapter) tql-img-path)
    (assoc adapter
      :input-cit input-cit
      :input-tql {:file-path tql-img-path}
      :discovered-classes display-names
      :used-scripts [(find-entry-point-module scripts)])))

(defn is-in-package [package-name adapter]
  (.contains (:path adapter) package-name))

(defn build-adapters&jobs-doc
  "Build data ready to be included in the documentation - list of pairs
adapter to jobs declared on top of it"
  [ddm-path cm-path package-name update-serialized?]

  (let [data (read-data ddm-path cm-path :update-serialized? update-serialized?)
        class-by-name (:class-by-name data)
        adapters (:adapters data)
        tql-by-name (:tql-by-name data)
        job-by-id (:job-by-id data)]
    (for [adapter (filter (partial is-in-package package-name) adapters)]
      (let [adapter (prepare-adapter adapter class-by-name)
            id (:id adapter)
            jobs (filter #(= id (:pattern-id %)) (vals job-by-id))
            adapter-doc (md-gen/generate-adapter adapter)
            jobs-doc (map (comp md-gen/generate-job
                                #(prepare-job % tql-by-name)) jobs)]
        [adapter-doc jobs-doc]))))

(defn build-doc
  "Build general documentation that includes
- concepts and introductional section
- reference part
  - adapters
  - jobs
  - application signature and plugins"
  [ddm-path cm-path package-name update-serialized?]
  (let [adapter-to-jobs-pairs (build-adapters&jobs-doc
                               ddm-path cm-path
                               package-name update-serialized?)
        reference-doc (md-gen/generate-reference adapter-to-jobs-pairs)
        general-doc (md-gen/generate-general reference-doc)]
    general-doc))
