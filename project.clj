(defproject radiosity "0.0.1-SNAPSHOT"
  :description "Simple radiosity implementation"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [integrant "0.6.3"]
                 [uncomplicate/clojurecl "0.8.0"]]

  :monkeypatch-clojure-test false

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]

  :main radiosity.main

  :uberjar-name "radiosity.jar"

  :profiles {:dev {:plugins      [[lein-ancient "0.6.15"]]}})
