(ns ddm-doc.md-generator
  (:use fleet))

(defn find-template [name]
  (.findResource (.getContextClassLoader (Thread/currentThread)) name))

(extend-protocol fleet.runtime/Screenable
  nil
  (screen [s escaping-fn] "None"))

(defn generate-adapter [adapter]
  (let [content (slurp (find-template "resources/adapter.md"))
        tpl (fleet [pattern] content)]
    (tpl adapter)))

(defn generate-job [job]
  (let [content (slurp (find-template "resources/job.md"))
        tpl (fleet [job] content)]
    (tpl job)))

(defn generate-reference [pattern-to-jobs-pairs]
  (let [content (slurp (find-template "resources/reference.md"))
        tpl (fleet [reference] content)]
    (tpl {:patterns (map first pattern-to-jobs-pairs)
          :jobs (mapcat second pattern-to-jobs-pairs)})))

(defn generate-general [reference]
  (let [content (slurp (find-template "resources/general.md"))
        tpl (fleet [reference] content)]
    (tpl {:content (str reference)})))
