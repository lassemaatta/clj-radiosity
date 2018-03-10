(ns radiosity.wavefront.file-reader
  (:require [clojure.string :as string]))

(defn read-lines [file-path]
  (->> (slurp file-path)
       (string/split-lines)
       (map string/trim)))
