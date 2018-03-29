(ns radiosity-ui.effects.interval
  (:require [re-frame.fx :as fx]
            [re-frame.router :as router]))

(defn- apply-and-clear
  "Apply an operation on the registry. If the registry already contained
  a handle with the given id, it is cleared."
  [registry id update-fn]
  (let [old-reg (first (swap-vals! registry update-fn))]
    (when-let [old-handle (get old-reg id)]
      (js/clearInterval old-handle))))

(defmulti handle-action (fn [ctx] (:action ctx)))

(defmethod handle-action :start [{:keys [registry id frequency event]}]
  (let [callback  #(router/dispatch event)
        handle    (js/setInterval callback frequency)
        update-fn #(assoc % id handle)]
    (apply-and-clear registry id update-fn)))

(defmethod handle-action :stop [{:keys [registry id]}]
  (let [update-fn #(dissoc % id)]
    (apply-and-clear registry id update-fn)))

; Expects [id frequency event] map
(fx/reg-fx
  ::interval
  (let [registry (atom {})]
    (fn [ctx]
      (let [ctx (assoc ctx :registry registry)]
        (handle-action ctx)))))
