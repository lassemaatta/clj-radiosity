(ns radiosity-ui.drawing
  (:require [thi.ng.geom.core :as geom]
            [thi.ng.geom.triangle :as tri]
            [thi.ng.geom.gl.glmesh :as glmesh]
            [thi.ng.geom.gl.shaders :as shaders]
            [thi.ng.geom.gl.webgl.constants :as glc]
            [thi.ng.geom.gl.camera :as cam]
            [thi.ng.geom.gl.core :as gl]))

(defonce triangle (geom/as-mesh
                    (tri/triangle3 [[1 0 0] [0 0 0] [0 1 0]])
                    {:mesh (glmesh/gl-mesh 3)}))

(def shader-spec
  {:vs       "void main() { gl_Position = proj * view * vec4(position, 1.0); }"
   :fs       "void main() { gl_FragColor = vec4(0.5, 0.5, 1.0, 1.0); }"
   :uniforms {:view :mat4
              :proj :mat4}
   :attribs  {:position :vec3}})

(defn- combine-model-shader-and-camera
  [gl-ctx model shader-spec camera]
  (let [shader (shaders/make-shader-from-spec gl-ctx shader-spec)]
    (-> model
        (gl/as-gl-buffer-spec {})
        (assoc :shader shader)
        (gl/make-buffers-in-spec gl-ctx glc/static-draw)
        (cam/apply camera))))

(defn draw [camera context attrs]
  (let [camera (cam/perspective-camera camera)
        scene  (combine-model-shader-and-camera context triangle shader-spec camera)]
    (doto context
      (.clearColor (rand) (rand) (rand) 1)
      (.clearDepth 1)
      (.clear (bit-or 0x100 0x4000))
      (gl/draw-with-shader scene))))
