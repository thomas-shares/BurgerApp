(ns app.component.Burger.build-controls
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))


(defmutation add-ingredient [props]
  (action [{:keys [state]}]
    (let [id (:id props)
          price (:price props)]
      ;(js/console.log id price (get-in @state [:order/id 1 :order/total-price]))
      (swap! state update-in [:order/id 1 :order/ingredients id :count] inc)
      (swap! state update-in [:order/id 1 :order/total-price] + price))))

(defmutation remove-ingredient [props]
  (action [{:keys [state]}]
     (let [id (:id props)
           price (:price props)]
       ;(js/console.log id price (get-in @state [:order/id 1 :order/total-price]))
       (swap! state update-in [:order/id 1 :order/ingredients id :count] dec)
       (swap! state update-in [:order/id 1 :order/total-price] - price))))


(defmutation toggle-modal [_]
  (action [{:keys [state]}]
    (let [modal? (get-in @state [:ui/id 1 :ui/modal?])]
      (js/console.log "Toggle Modal: "  modal?)
      (swap! state assoc-in [:ui/id 1 :ui/modal?] (not modal?)))))


(defsc BuildControl [this props]
  {}
  ;(js/console.log "control: " props)
  (dom/div {:className "BuildControl"}
    (dom/div {:className "Label"} (:ingredient props))
    (dom/button {:className "Less"
                 :disabled (zero? (:count props))
                 :onClick #(comp/transact! this [(remove-ingredient props)])} "Less")
    (dom/button {:className "More"
                 :onClick #(comp/transact! this [(add-ingredient props)])} "More")))

(def ui-build-control (comp/factory BuildControl))

(defsc BuildControls [this {:order/keys [burger] :as props}]
  {:query [:control/id :order/burger]
   :ident :control/id
   :initial-state {}}
  ;(js/console.log  "BuildControls: "  (:order/total-price burger))
  (let [ingredients (:order/ingredients burger)]
    (dom/div {:className "BuildControls"}
      (dom/p "Current Price: " (dom/strong (.toFixed (:order/total-price burger) 2)))
      (map ui-build-control ingredients)
      (dom/button {:className "OrderButton"
                   :disabled (zero? (reduce + 0 (map #(:count %) ingredients)))
                   :onClick #(comp/transact! this [(toggle-modal)])} "Order Now"))))

(def ui-build-controls (comp/factory BuildControls))
