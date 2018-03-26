(ns radiosity-ui.views
  (:require [re-frame.core :as re-frame]
            [radiosity-ui.component.canvas :refer [my-canvas]]
            [radiosity-ui.component.camera :as camera]
            [radiosity-ui.drawing :as drawing]))


(defn main-panel []
  (let [camera-id    "cam-1"
        camera-coord (re-frame/subscribe [::camera/camera camera-id])]
    (re-frame/dispatch [::camera/init camera-id])
    (fn []
      (let [draw-fn (partial drawing/draw @camera-coord)]
        [:div
         [camera/camera camera-id]
         [my-canvas {:id      "mycanvas2"
                     :width   "500px"
                     :height  "500px"
                     :draw-fn draw-fn}]]))))
