(ns app.app
  (:require [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.rendering.keyframe-render2 :refer [render!]]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]))

(defonce app (app/fulcro-app {:optimized-render! render!
                              :client-did-mount  (fn
                                                   [app]
                                                   (dr/change-route app [""]))}))
