(ns radiosity.openc
  (:require [uncomplicate.clojurecl.core :as ucore]
            [uncomplicate.clojurecl.info :as uinfo]
            [uncomplicate.commons.core :as commons]))



(let [platform      (first (ucore/platforms))
      platform-info (uinfo/name-info platform)
      device        (first (ucore/devices platform))
      device-info   (uinfo/name-info device)
      ctx           (ucore/context [device])]
  (clojure.pprint/pprint platform-info)
  (commons/release ctx))
