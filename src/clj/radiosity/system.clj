(ns radiosity.system
  (:require [integrant.core :as ig]))

(def config {})

(def system
  (ig/init config))
