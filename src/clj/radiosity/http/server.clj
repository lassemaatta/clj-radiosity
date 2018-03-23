(ns radiosity.http.server
  (:require [ring.adapter.jetty :as jetty]
           [integrant.core :as ig]))

(defmethod ig/init-key :adapter/jetty [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc :handler) (assoc :join? false))))
