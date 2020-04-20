(ns app.app
  (:require [com.fulcrologic.fulcro.application :as app]
            [com.fulcrologic.fulcro.rendering.keyframe-render2 :refer [render!]]))


(defonce app (app/fulcro-app {:optimized-render! render!}))
