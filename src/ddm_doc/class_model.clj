(ns ddm-doc.class-model
  (:use [clojure.xml :only [parse]]
        [ddm-doc.xml :only [tf->]]
        [clojure.pprint :only [pprint]]))


(defn classes? [el]
  (= :Classes (:tag el)))


(defn get-class-els
  "@types: dict -> dict"
  [elms]
  (:content (first (filter classes? (:content elms)))))


(defn parse-class-definition
  "Parse class definition and returns dict of such keys
  :parent-class - derived from class
  :class-name   - class name
  :display-name
  :description
  "
  [elm]
  (let [attrs (select-keys (:attrs elm) [:class-name :display-name :description])
        parent-class (get-in (tf-> (:content elm) :Derived-From) [:attrs :class-name])]
    (assoc attrs :parent-class parent-class)))


(defn parse-class-model [source]
  (let [elms (parse source)
        class-definition-elms (get-class-els elms)]
    (map parse-class-definition class-definition-elms)))
