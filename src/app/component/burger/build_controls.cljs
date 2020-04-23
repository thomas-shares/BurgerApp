(ns app.component.burger.build-controls
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))


(defmutation add-ingredient [props]
  (action [{:keys [state]}]
    (let [id (:id props)
          price (:price props)]
      (swap! state update-in [:order/id 1 :order/ingredients id :count] inc)
      (swap! state update-in [:order/id 1 :order/total-price] + price))))

(defmutation remove-ingredient [props]
  (action [{:keys [state]}]
     (let [id (:id props)
           price (:price props)]
       (swap! state update-in [:order/id 1 :order/ingredients id :count] dec)
       (swap! state update-in [:order/id 1 :order/total-price] - price))))

(defmutation toggle-modal [_]
  (action [{:keys [state]}]
    (let [modal? (get-in @state [:singleton :app.ui/modal :ui/modal])]
      (swap! state assoc-in [:singleton :app.ui/modal :ui/modal] (not modal?)))))

(defsc BuildControl [this props]
  {}
  (dom/div {:className "BuildControl"}
    (dom/div {:className "Label"} (:ingredient props))
    (dom/button {:className "Less"
                 :disabled (zero? (:count props))
                 :onClick #(comp/transact! this [(remove-ingredient props)])} "Less")
    (dom/button {:className "More"
                 :onClick #(comp/transact! this [(add-ingredient props)])} "More")))

(def ui-build-control (comp/factory BuildControl))

(defsc BuildControls [this {:order/keys [ingredients total-price] :as props}]
  {:query [:order/ingredients :order/total-price]
   :ident (fn [] [:singleton ::control])
   :initial-state {}}
  (dom/div {:className "BuildControls"}
    (dom/p "Current Price: " (dom/strong (.toFixed total-price 2)))
    (map ui-build-control ingredients)
    (dom/button {:className "OrderButton"
                 :disabled (zero? (reduce + 0 (map #(:count %) ingredients)))
                 :onClick #(comp/transact! this [(toggle-modal)])} "Order Now")))

(def ui-build-controls (comp/factory BuildControls))
