(ns ddm-doc.t-core
  (:use midje.sweet)
  (:use [ddm-doc.core]))

(defn- get-plugins []
  [{:id "plugin_id1",
    :qualifiers [["application" "app-id"]
                 ["application" "app-id3"]
                 ["application" "app-id4"]
                 ["protocol" "proto-1"]
                 ["protocol" "proto-2"]], }

   {:id "plugin_id2",
    :qualifiers [["application" "app-id"]
                 ["application" "app-id2"]
                 ["application" "app-id3"]
                 ["protocol" "proto-1"]
                 ["protocol" "proto-2"]], }
   ])


(fact "plugins sorted by app"
      (group-by-app-qualifier (get-plugins)) =>

        {"app-id" [{:active-qualifier "app-id", :id "plugin_id1",
                        :qualifiers [["application" "app-id"] ["application" "app-id3"] ["application" "app-id4"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}
                   {:active-qualifier "app-id", :id "plugin_id2",
                        :qualifiers [["application" "app-id"] ["application" "app-id2"] ["application" "app-id3"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}],
         "app-id2" [{:active-qualifier "app-id2", :id "plugin_id2",
                        :qualifiers [["application" "app-id"] ["application" "app-id2"] ["application" "app-id3"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}],
         "app-id3" [{:active-qualifier "app-id3", :id "plugin_id1",
                        :qualifiers [["application" "app-id"] ["application" "app-id3"] ["application" "app-id4"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}
                    {:active-qualifier "app-id3", :id "plugin_id2",
                        :qualifiers [["application" "app-id"] ["application" "app-id2"] ["application" "app-id3"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}],
         "app-id4" [{:active-qualifier "app-id4", :id "plugin_id1",
                        :qualifiers [["application" "app-id"] ["application" "app-id3"] ["application" "app-id4"] ["protocol" "proto-1"] ["protocol" "proto-2"]]}]
         }
      )
