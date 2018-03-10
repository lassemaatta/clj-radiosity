(ns radiosity.wavefront.reader
  (:require [radiosity.wavefront.file-reader :as file-reader]
            [radiosity.wavefront.core :as wavefront]))


(defn has-content? [[_ content]]
  (some? content))

(defn with-line-numbers [coll]
  (map list (iterate inc 0) coll))

(let [items-by-type (->> "resources/obj_examples/cube.obj"
                         (file-reader/read-lines)
                         (map wavefront/line->element)
                         (filter some?)
                         (group-by :type))
      vertices      (:vertex items-by-type)
      faces         (:face items-by-type)]
  (clojure.pprint/pprint vertices)
  (clojure.pprint/pprint faces))

