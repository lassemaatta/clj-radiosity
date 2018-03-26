(ns radiosity-ui.component.canvas
  (:require [reagent.core :as reagent]
            [thi.ng.geom.gl.core :as gl]))

(defn- did-mount
  "Register a WebGL context to the canvas once it's mounted to the DOM"
  [this element context id]
  (reset! element (reagent/dom-node this))
  (reset! context (gl/gl-context id)))

(defn- render
  "Return the component"
  [draw {:keys [id draw-fn width height] :or {width  "200px"
                                              height "100px"}}]
  (reset! draw draw-fn)
  [:canvas {:id     id
            :width  width
            :height height}])

(defn- did-update
  "Draw the scene once the component is mounted and rendered"
  [draw-fn context element]
  (when (and draw-fn context element)
    (let [attrs (-> {}
                    (assoc :width (.-clientWidth element))
                    (assoc :height (.-clientHeight element)))]
      (draw-fn context attrs))))

(defn my-canvas [{:keys [id draw-fn]}]
  (let [context    (atom nil)                               ; holds the current gl-context
        element    (atom nil)                               ; hold the DOM element
        draw       (atom draw-fn)
        class-spec (-> {}
                       (assoc :display-name "radiosity-canvas")
                       ; Invoke when component is mounted into the DOM
                       (assoc :component-did-mount #(did-mount % element context id))
                       ; Invoke when the component properties changed
                       (assoc :reagent-render #(render draw %))
                       ; Invoke after DOM rendering is complete
                       (assoc :component-did-update #(did-update @draw @context @element)))]
    (reagent/create-class class-spec)))

