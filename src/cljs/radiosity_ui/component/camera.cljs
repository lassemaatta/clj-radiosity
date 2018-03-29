(ns radiosity-ui.component.camera
  (:require [re-frame.core :as re-frame]
            [radiosity-ui.effects.interval :as interval]
            [thi.ng.geom.core :as g]
            [thi.ng.geom.vector :refer [vec3]]))

(def ^:private DEFAULT_CAMERA {:eye (vec3 0 0 2)})

(defn- rotate-eye [camera dir]
  (let [amount  0.05
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

(defn- rotate-camera
  [db [_ dir id]]
  (let [camera  (get-in db [::camera id] DEFAULT_CAMERA)
        rotated (rotate-eye camera dir)]
    (assoc-in db [::camera id] rotated)))

(defn- timer-id [id]
  (str ::interval "-" id))

;; Events

; Initialize camera
(re-frame/reg-event-db ::init init)

(re-frame/reg-event-db ::camera-rotate rotate-camera)

(re-frame/reg-event-fx
  ::camera-rotate-start
  (fn [_ [_ dir id]]
    {::interval/interval {:action    :start
                          :id        (timer-id id)
                          :frequency 100
                          :event     [::camera-rotate dir id]}}))

(re-frame/reg-event-fx
  ::camera-rotate-stop
  (fn [_ [_ id]]
    {::interval/interval {:action :stop
                          :id     (timer-id id)}}))

;; subs

(re-frame/reg-sub
  ::camera
  (fn [db [_ id]]
    (get-in db [::camera id])))

;; view

(defn- direction [id dir]
  [:input {:type          :button
           :value         dir
           :on-mouse-down #(re-frame/dispatch [::camera-rotate-start dir id])
           :on-mouse-up   #(re-frame/dispatch [::camera-rotate-stop id])}])

(defn camera [id]
  (let [direction (partial direction id)]
    (into [:div] (map direction [::up ::down ::left ::right ::reset]))))
