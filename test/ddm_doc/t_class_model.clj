(ns ddm-doc.t-class-model
  (:require [ddm-doc.class-model :as cm]
            [clojure.xml :refer [parse]]
            [midje.sweet :refer :all]))

(defn get-test-class-def []
  (let [definition (parse "test/resources/class_def.xml")]
    definition))

(facts "parsing facts"
       (fact "parse class definition"
             (cm/parse-class-definition (get-test-class-def)) =>
             {:class-name "class_name", :description "-description", :display-name "ClassDisplayName"
              :parent-class "base_class"}))
