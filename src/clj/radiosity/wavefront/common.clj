(ns radiosity.wavefront.common
  (:require [radiosity.utils.string :as utils]
            [clojure.spec.alpha :as s]))

;;; Spec

(s/def ::type keyword?)
(s/def ::coordinate number?)
(s/def ::index pos-int?)

(defmulti element-type ::type)

(s/def ::element (s/multi-spec element-type ::type))


;; Impl

(defn- dispatch-by-first-field [line]
  (let [key (first (utils/split-at-whitespace line))]
    key))

(defmulti process-line dispatch-by-first-field)

(defmethod process-line :default [_]
  nil)

