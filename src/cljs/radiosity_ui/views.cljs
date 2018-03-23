(ns radiosity-ui.views
  (:require [re-frame.core :as re-frame]
            [radiosity-ui.subs :as subs]
            [radiosity-ui.canvas :refer [my-canvas]]))



(defn main-panel []
  (let [name       (re-frame/subscribe [::subs/name])
        name-upper (re-frame/subscribe [::subs/name-upper])]
    [:div
     [:div "Hello from " @name-upper]
     [:br]
     [:input {:type      :text
              :value     @name
              :on-change #(re-frame/dispatch [:changed (-> % .-target .-value)])}]
     [my-canvas "mycanvas2" @name]]))
