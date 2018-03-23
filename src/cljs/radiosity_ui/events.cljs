(ns radiosity-ui.events
  (:require [re-frame.core :as re-frame]
            [radiosity-ui.db :as db]))

(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :changed
  (fn [db [_ val]]
    ;(draw)
    (assoc db :name val)))
