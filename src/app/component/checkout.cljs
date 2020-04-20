(ns app.component.checkout
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr :refer [defrouter]]))

(defsc Checkout [this {:order/keys [burger] :as props}]
  {:query [:order/burger]
   :ident (fn [] [:singleton ::checkout])
   :initial-state {}
   :route-segment ["checkout"]}
  (dom/div "Checkout!!!"))




(def ui-checkout (comp/factory Checkout))
