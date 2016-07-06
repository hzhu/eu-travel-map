(ns app.core
  (:require
   [reagent.core :as reagent]
   [data.core :as data]
   [clojure.string :as s]
   [cljsjs.d3 :as d3]
   [cljsjs.topojson :as topojson]))
(enable-console-print!)

(defn create-eu-map [ratom path svg]
  (.. js/d3
    (json "eu.json"
          (fn [eu-data]
            (let [topjson-transform (.. js/topojson (feature eu-data (aget eu-data "objects" "europe")) -features)]
              (.. svg
                  (selectAll "country")
                  (data topjson-transform)
                  enter
                  (append "path")
                  (attr "d" path)
                  (attr "class" "country")
                  (attr "data-name" (fn [d]
                                      (aget d "properties" "name")))
                  (style "stroke" "black")
                  (style "opacity" 0.5)
                  (style "fill" "black")
                  (style "fill" (fn [d]
                                  (let [data (@ratom :countries)
                                        country (aget d "properties" "name")]
                                      (data country))))))))))


(defn create-flags [data svg projection]
   (let [topojson-transform ( .. js/topojson (feature data (aget data "objects" "places")) -features)]
     (.. js/d3
         (select "svg")
         (selectAll ".flags")
         (data topojson-transform)
         enter
         (append "image")
         (attr "width" 18)
         (attr "height" 11)
         (attr "transform" (fn [d]
                               (let [coordinates (aget d "geometry" "coordinates")
                                     pos (projection coordinates)]
                                 (aset pos 0 (- (aget pos 0) 15))
                                 (aset pos 1 (- (aget pos 1)  5))
                                 (str "translate(" pos ")"))))
         (attr "xlink:href" (fn [d]
                             (let [value (aget d "properties" "country")
                                   country-lower (s/lower-case value)
                                   country (s/replace country-lower #" " "_")]
                                 (str "/flags/" country ".svg")))))))


(defn create-labels [svg projection]
  (.. js/d3
    (json "cities.json"
          (fn [err data]
            (if (not (nil? err))
              (.error js/console err))
            (create-flags data svg projection)))))


(defn did-mount-fn [ratom]
  (let [width 800
        height 600
        center (clj->js [15, 61])
        scale 1200
        projection (.center (.translate (.scale ((aget js/d3 "geo" "mercator")) scale) (clj->js [(quot width 2) 0])) center)
        path (.projection ((aget js/d3 "geo" "path")) projection)
        svg (.. js/d3
              (select "#map")
              (append "svg")
              (attr "height" height)
              (attr "width" width)
              (append "g"))]
    (create-eu-map ratom path svg)
    (create-labels svg projection)))


(defn eu-map [ratom]
  (reagent/create-class {
      :reagent-render (fn [] [:div {:id "map"}])
      :component-did-mount #(did-mount-fn ratom)}))


(defn ^:export main []
  (reagent/render [eu-map data/app-state]
  (.getElementById js/document "app")))