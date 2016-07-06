(ns data.core
  (:require
       [reagent.core :as reagent]))

(defonce app-state
  (reagent/atom {
    :countries {"Czech Rep." "mediumturquoise"
                "Hungary" "plum"
                "Austria" "darkgoldenrod"
                "Belgium" "darkgreen"
                "Netherlands" "darkviolet"
                "Germany" "royalblue"
                "Poland" "darkred"
                "Lithuania" "blue"
                "Latvia" "purple"
                "Estonia" "chocolate"
                "Denmark" "yellow" }
}))