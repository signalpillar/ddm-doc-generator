(ns ddm-doc.xml)

(defn tf->
  "Fidn first element with tag name specified"
  [elms tag-name]
  (first (filter #(= (:tag %) tag-name) elms)))
