(ns app.component.checkout
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.burger.burger :refer [Burger ui-burger]]))

(defsc Checkout [this {:order/keys [burger] :as props}]
  {:query [{:order/burger (comp/get-query Burger)}]
   :ident (fn [] [:singleton ::checkout])
   :initial-state (fn [_] {:order/burger (comp/get-initial-state Burger)})
   :route-segment ["checkout"]}
  (dom/div {:className "CheckoutSummary"}
    (dom/h1 "We hope it tastes well!!!")
    (dom/div {:style {:width "100%" :margin :auto}}
      (ui-burger burger))
    (dom/a {:href "/"}
      (dom/button {:classes ["Button Danger"]} "CANCEL"))
    (dom/a {:href "/checkout/contact-details"}
      (dom/button {:classes ["Button Success"]} "CONTINUE"))))

(def ui-checkout (comp/factory Checkout))
