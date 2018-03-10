(ns radiosity.wavefront.core
  (:require [radiosity.wavefront.common :as common]
            [radiosity.wavefront.elements.faces]
            [radiosity.wavefront.elements.vertices]
            [clojure.spec.alpha :as s]))


(defn line->element [line]
 (common/process-line line))

(s/fdef line->element
        :args (s/cat :line string?)
        :ret ::common/element)
