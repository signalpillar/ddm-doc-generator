(ns ddm-doc.class-model
  (:use [clojure.xml :only [parse]]
        [clojure.pprint :only [pprint]]))

(defn classes? [el]
  (= :Classes (:tag el)))

(defn get-class-els
  "@types: dict -> dict"
  [elms]
  (:content (first (filter classes? (:content elms)))))

(defn parse-class-definition [elm]
  (select-keys (:attrs elm) [:class-name :display-name :description]))

(defn parse-class-model [source]
  (let [elms (parse source)
        class-definition-elms (get-class-els elms)]
    (map parse-class-definition class-definition-elms)))
