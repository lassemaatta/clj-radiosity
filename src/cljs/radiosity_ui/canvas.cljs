(ns radiosity-ui.canvas
  (:require [reagent.core :as reagent]
            [thi.ng.geom.core :as geom]
            [thi.ng.geom.triangle :as tri]
            [thi.ng.geom.gl.glmesh :as glmesh]
            [thi.ng.geom.gl.shaders :as shaders]
            [thi.ng.geom.gl.webgl.constants :as glc]
            [thi.ng.geom.gl.camera :as cam]
            [thi.ng.geom.gl.core :as gl]))

(defonce camera (cam/perspective-camera {}))

(def triangle (geom/as-mesh
                (tri/triangle3 [[1 0 0] [0 0 0] [0 1 0]])
                {:mesh (glmesh/gl-mesh 3)}))

(def shader-spec
  {:vs       "void main() {
          gl_Position = proj * view * vec4(position, 1.0);
       }"
   :fs       "void main() {
           gl_FragColor = vec4(0.5, 0.5, 1.0, 1.0);
       }"
   :uniforms {:view :mat4
              :proj :mat4}
   :attribs  {:position :vec3}})

(defn combine-model-shader-and-camera
  [gl-ctx model shader-spec camera]
  (-> model
      (gl/as-gl-buffer-spec {})
      (assoc :shader (shaders/make-shader-from-spec gl-ctx shader-spec))
      (gl/make-buffers-in-spec gl-ctx glc/static-draw)
      (cam/apply camera)))

(defn draw [context model]
  (doto context
    (.clearColor (rand) (rand) (rand) 1)
    (.clearDepth 1)
    (.clear (bit-or 0x100 0x4000))
    (gl/draw-with-shader (combine-model-shader-and-camera context triangle shader-spec camera))))

(defn- my-render [model id val]
  (println "render" id val)
  (reset! model val)
  [:canvas {:id id :width "100px" :height "100px"}])

(defn- did-mount [context id]
  (println "did mount!" id)
  (reset! context (gl/gl-context id)))

(defn- did-update [context model]
  (println "did update!" model)
  (when (and context model)
    (draw context model)))

(defn my-canvas [id _]
  (let [context (atom nil)                                  ; holds the current gl-context
        model   (atom nil)                                  ; holds the current model
        spec    (-> {}
                    (assoc :display-name "gl-canvas")
                    ; render occurs when the model changes
                    (assoc :reagent-render #(my-render model %1 %2))
                    ; Initialize context when component is mounted
                    (assoc :component-did-mount #(did-mount context id))
                    (assoc :component-did-update #(did-update @context @model)))]
    (reagent/create-class spec)))

