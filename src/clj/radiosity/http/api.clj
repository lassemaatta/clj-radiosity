(ns radiosity.http.api
  (:require [compojure.api.sweet :as sweet]
            [ring.util.http-response :as resp]
            [compojure.route :as r]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [integrant.core :as ig]))

(sweet/defapi app
  (sweet/GET "/" [] (resource-response "index.html" {:root "public"}))
  (r/resources "/")
  (sweet/GET "/hello" []
    :query-params [name :- String]
    (resp/ok {:message (str "Hello, " name)})))

(def dev-handler (-> #'app wrap-reload))

(def handler app)

(defmethod ig/init-key :component/api [_ config]
  app)


