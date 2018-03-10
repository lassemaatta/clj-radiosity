(ns radiosity.wavefront.elements.faces
  (:require [radiosity.wavefront.common :as common]
            [radiosity.utils.string :as utils]
            [clojure.string :as string]
            [clojure.spec.alpha :as s]))

(s/def ::v ::common/index)
(s/def ::t ::common/index)
(s/def ::n ::common/index)

(s/def ::face (s/keys :req-un [::v
                               ::t
                               ::n]))

(s/def ::a ::face)
(s/def ::b ::face)
(s/def ::c ::face)

(defmethod common/element-type :face [_]
  (s/keys :req-un [::common/type ::a ::b ::c]))

(defn- int-or-nil [s]
  (if (empty? s)
    nil
    (Integer/parseInt s)))

(defn- get-refs [s]
  (let [[v t n] (->> (string/split s #"/")
                     (map int-or-nil))]
    {:v v :t t :n n}))

(defn- parse-face [line]
  (let [[_ a b c] (utils/split-at-whitespace line)]
    {:type :face
     :a    (get-refs a)
     :b    (get-refs b)
     :c    (get-refs c)}))

(defmethod common/process-line "f" [line]
  (parse-face line))
