(ns radiosity.wavefront.elements.vertices
  (:require [radiosity.wavefront.common :as common]
            [radiosity.utils.string :as utils]
            [clojure.spec.alpha :as s]))

(s/def ::x ::common/coordinate)
(s/def ::y ::common/coordinate)
(s/def ::z ::common/coordinate)

(defmethod common/element-type :vertex [_]
  (s/keys :req-un [::common/type ::x ::y ::z]))

(defn- parse-vertex [line]
  (let [[_ x y z] (utils/split-at-whitespace line)]
    {:type :vertex
     :x    (Float/parseFloat x)
     :y    (Float/parseFloat y)
     :z    (Float/parseFloat z)}))

(defmethod common/process-line "v" [line]
  (parse-vertex line))
