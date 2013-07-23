(ns ddm-doc.md-generator
  (:use fleet))

(defn generate-doc [adapter jobs]
  (let [content (slurp "src/resources/adapter.md")
        tpl (fleet [adapter] content)]
    tpl))
