(ns radiosity.system
  (:require [integrant.core :as ig]
            [radiosity.http.server]
            [radiosity.http.api]))

(def config {:adapter/jetty {:port 8080, :handler (ig/ref :component/api)}
             :component/api {}})

(def system
  (ig/init config))
