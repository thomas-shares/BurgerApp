(ns app.component.main
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.burger.burger :refer [ui-burger Burger]]
            [app.component.burger.build-controls :refer [ui-build-controls BuildControls]]
            [app.ui :refer [ui-modal Modal]]))

(defsc Main [this {:order/keys [burger]
                   :ui/keys [modal] :as props}]
  {:query [{:order/burger (comp/get-query Burger)}
           {:ui/modal (comp/get-query Modal)}]
   :initial-state (fn [_] {:order/burger (comp/get-initial-state Burger)
                           :ui/modal (comp/get-initial-state Modal)})
   :route-segment ["order"]}
  ;;(js/console.log "main" props)
  (dom/div
    (ui-burger burger)
    (ui-build-controls burger)
    (ui-modal modal)))

(def ui-main (comp/factory Main))
