(defproject radiosity "0.0.1-SNAPSHOT"
  :description "Simple radiosity implementation"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [integrant "0.6.3"]
                 [yogthos/config "1.1.1"]
                 [ring "1.6.3"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [compojure "1.6.0"]
                 [metosin/compojure-api "1.1.12"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [uncomplicate/clojurecl "0.8.0"]
                 [thi.ng/geom "0.0.1178-SNAPSHOT"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :pedantic :abort
  :monkeypatch-clojure-test false
  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs     ["resources/public/css"]
             :ring-handler radiosity.http.api/dev-handler}


  :main radiosity.main
  :aot [radiosity.main]

  :uberjar-name "radiosity.jar"

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.9"]
                                  [day8.re-frame/re-frame-10x "0.3.0"]]

                   :plugins      [[javax.servlet/javax.servlet-api "4.0.0"]
                                  [cheshire "5.8.0"]
                                  [ring/ring-mock "0.3.2"]
                                  [lein-figwheel "0.5.15"]]}}

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]
                        :figwheel     {:on-jsload "radiosity-ui.core/mount-root"}
                        :compiler     {:main                 radiosity-ui.core
                                       :output-to            "resources/public/js/compiled/app.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :asset-path           "js/compiled/out"
                                       :source-map-timestamp true
                                       :preloads             [devtools.preload
                                                              day8.re-frame-10x.preload]
                                       :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                                       :external-config      {:devtools/config {:features-to-install :all}}}}

                       {:id           "min"
                        :source-paths ["src/cljs"]
                        :jar          true
                        :compiler     {:main            radiosity-ui.core
                                       :output-to       "resources/public/js/compiled/app.js"
                                       :optimizations   :advanced
                                       :closure-defines {goog.DEBUG false}
                                       :pretty-print    false}}]}

  :prep-tasks [["cljsbuild" "once" "min"] "compile"])
