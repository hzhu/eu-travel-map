(ns app.core
  (:require
   [reagent.core :as reagent]
   [cljsjs.d3 :as d3]
   [cljsjs.topojson :as topojson]
   [data.core :as data]))
(enable-console-print!)

(defonce app-state
  (reagent/atom
   {:text "Hello, what is your name? "}))

(defn did-mount-fn [ratom]
  (let   [width 800
          height 600
          center (clj->js [15, 61])
          scale 1200
          projection (.center (.translate (.scale ((aget js/d3 "geo" "mercator")) scale) (clj->js [(quot width 2) 0])) center)
          path (.projection ((aget js/d3 "geo" "path")) projection)
          svg (.. js/d3
                (select "#map")
                (append "svg")
                (attr "height" height)
                (attr "width" width))
          countries (.append svg "g")        
          ]
    
    (.json js/d3 "eu.json" 
      (fn [eu-data] 
        (.selectAll countries "country")
        (.log js/console countries)))
 
    )
)

(defn eu-map [ratom]
  (reagent/create-class { 
      :reagent-render (fn [] [:div {:id "map"}])
      :component-did-mount #(did-mount-fn ratom)}))

(defn ^:export main []
  (reagent/render [eu-map data/app-state]
  (.getElementById js/document "app")))