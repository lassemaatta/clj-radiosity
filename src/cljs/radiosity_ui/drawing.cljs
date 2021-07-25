(ns radiosity-ui.drawing
  (:require [thi.ng.geom.core :as geom]
            [thi.ng.geom.triangle :as tri]
            [thi.ng.geom.vector :as vec]
            [thi.ng.geom.gl.glmesh :as glmesh]
            [thi.ng.geom.gl.shaders :as shaders]
            [thi.ng.geom.gl.webgl.constants :as glc]
            [thi.ng.geom.gl.camera :as cam]
            [thi.ng.geom.gl.core :as gl]
            [thi.ng.color.core :as col]
            [thi.ng.typedarrays.core :as arrays]))

(defonce triangle (geom/as-mesh
                    (tri/triangle3 [[1 0 0] [0 0 0] [0 1 0]])
                    {:mesh    (glmesh/gl-mesh 3 #{:col :fnorm})
                     :res     40
                     :attribs {:col (fn [_] (col/rgba 0.1 0.9 0.1))}}))


(defonce triangle2 (geom/as-mesh
                     (tri/triangle3 [[1.5 0.5 0.5] [0.5 0.5 0.5] [0.5 1.5 0.5]])
                     {:mesh    (glmesh/gl-mesh 3 #{:col :fnorm})
                      :res     40
                      :attribs {:col (fn [_] (col/rgba 0.9 0 0.1))}}))

(def shader-spec
  {:vs       "void main() { gl_Position = proj * view * vec4(position, 1.0); vcolor = color;}"
   :fs       "void main() { gl_FragColor = vcolor; }"
   :uniforms {:view :mat4
              :proj :mat4}
   :attribs  {:position :vec3
              :color    :vec4}
   :varying  {:vcolor :vec4}})

(defonce verts [1.5 0.5 0.5 0.5 0.5 0.5 0.5 1.5 0.5])
(defonce cols (flatten (repeat 3 [0.9 0.1 0.1 0.9])))

(defonce foobar (-> {:attribs      {:position {:data (arrays/float32 verts) :size 3}
                                    :color    {:data (arrays/float32 cols) :size 4}}
                     :mode         glc/triangles
                     :num-vertices (/ (count verts) 3)}))

(defn- combine-model-shader-and-camera2
  [context shader camera model]
  (let [result
        (-> foobar
            ;(gl/as-gl-buffer-spec {})
            (assoc :shader shader)
            (gl/make-buffers-in-spec context glc/static-draw)
            (cam/apply camera))]
    ;(cljs.pprint/pprint (dissoc result :shader))
    result))

(defn- combine-model-shader-and-camera
  [context shader camera model]
  (let [result
        (-> model
            (gl/as-gl-buffer-spec {})
            (assoc :shader shader)
            (gl/make-buffers-in-spec context glc/static-draw)
            (cam/apply camera))]
    (cljs.pprint/pprint (dissoc result :shader))
    result))

(defn draw [camera context attrs]
  (let [camera (cam/perspective-camera camera)
        shader (shaders/make-shader-from-spec context shader-spec)
        models (vector (geom/into triangle2 triangle))
        scenes (map (partial combine-model-shader-and-camera context shader camera) models)
        sc2    (shuffle scenes)]
    (doto context
      (.clearColor (rand) (rand) (rand) 1)
      (.clearDepth 1)
      (.clear (bit-or 0x100 0x4000))
      (gl/draw-with-shader (first sc2))
      (gl/draw-with-shader (second sc2)))))
