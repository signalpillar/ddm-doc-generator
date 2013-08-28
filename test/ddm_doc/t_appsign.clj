(ns ddm-doc.t_appsign
  (:require [ddm-doc.appsigs :as as]
            [clojure.xml :refer [parse]]
            [midje.sweet :refer :all]))

(defn get-def [] (parse "test/resources/appsign.xml"))
(defn get-plugin-def [] (parse "test/resources/plugin_def.xml"))

(facts "parse plugin descriptor"
       (fact "simple descriptor parsed"
             (vec (as/parse-plugin-descriptor (get-plugin-def))) =>
    [
     {:dependencies ["x" "y"],
      :id "plugin_id1",
      :qualifiers [["application" "app-id"]
                   ["protocol" "proto-1"]
                   ["protocol" "proto-2"]],
      :module "plugin_module",
      :class "PluginClass",
      :name "-name",
      :description "-descr"}

     {:dependencies ["x" "y"],
      :id "plugin_id1",
      :qualifiers [["application" "app-id"]
                   ["protocol" "proto-1"]
                   ["protocol" "proto-2"]],
      :module "plugin_module",
      :class "PluginClass",
      :name "-name",
      :description "-descr"}]
         ))

(facts "parse appsign descriptor"
       (fact "2 components parsed"
             (vec (as/parse-sign-descriptor (get-def))) =>
 [{:app_id "-app-id",
   :attributes [{:name "category",
                 :type "string",
                 :value "Data Services"}],
   :category "-category",
   :ci_type "-ci-type",
   :name "-name",
   :processes [{:description "Service runner",
                :main-process "true",
                :name "process.exe",
                :ports "None",
                :required "true"}
               {:description "Starts the data movement engine that integrates data from multiple heterogeneous source",
                :name "process2.exe",
                :ports "all",
                :required "true"}],
   :supported_versions "xx",
   :vendor "-vendor"}
  {:app_id "-app-id",
   :attributes [{:name "category",
                 :type "string",
                 :value "Data Services"}],
   :category "-category",
   :name "-name",
   :processes [{:description "Service runner",
                :main-process "true",
                :name "process.exe",
                :ports "None",
                :required "true"}
               {:description "Starts the data movement engine that integrates data from multiple heterogeneous source"
                :name "process2.exe",
                :ports "all",
                :required "true"}],
   :supported_versions "xx",
   :vendor "-vendor"}]))
