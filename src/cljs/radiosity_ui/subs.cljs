(ns radiosity-ui.subs
  (:require [re-frame.subs :as re-frame]
            [clojure.string :as string]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::name-upper
  :<- [::name]
  (fn [name]
    (string/upper-case name)))
