(ns radiosity.utils.string
  (:require [clojure.string :as string]))

(defn split-at-whitespace [s]
  (let [fields (string/split s #" ")]
    (filter not-empty fields)))
