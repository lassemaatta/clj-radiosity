(ns radiosity-ui.component.camera
  (:require [re-frame.core :as re-frame]
            [thi.ng.geom.core :as g]
            [thi.ng.geom.vector :refer [vec3]]))

(def ^:private DEFAULT_CAMERA {:eye (vec3 0 0 2)})

(defn- rotate-camera [camera dir]
  (let [amount  0.1
        eye     (:eye camera)
        new-eye (condp = dir
                  ::up (g/rotate-x eye amount)
                  ::down (g/rotate-x eye (- amount))
                  ::left (g/rotate-y eye amount)
                  ::right (g/rotate-y eye (- amount))
                  ::reset (:eye DEFAULT_CAMERA))]
    (assoc camera :eye new-eye)))

(defn- init
  [db [_ id]]
  (assoc-in db [::camera id] DEFAULT_CAMERA))

(defn- camera-dir
  [db [_ dir id]]
  (let [camera  (get-in db [::camera id] DEFAULT_CAMERA)
        rotated (rotate-camera camera dir)]
    (assoc-in db [::camera id] rotated)))

(re-frame/reg-event-db ::init init)

(re-frame/reg-event-db ::camera-dir camera-dir)

;; subs

(re-frame/reg-sub
  ::camera
  (fn [db [_ id]]
    (get-in db [::camera id])))

;; view

(defn- direction [id dir]
  [:input {:type     :button
           :value    dir
           :on-click #(re-frame/dispatch [::camera-dir dir id])}])

(defn camera [id]
  (let [direction (partial direction id)]
    (into [:div] (map direction [::up ::down ::left ::right ::reset]))))
