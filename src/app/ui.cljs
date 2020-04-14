(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.burger.build-controls :refer [toggle-modal]]
            [app.component.burger.burger :refer [Burger]]))



(defsc Backdrop [this {:ui/keys [modal] :as props}]
  {}
  (when modal
    (dom/div {:className "Backdrop" :onClick
                         #(comp/transact! this [(toggle-modal props)])})))

(def ui-backdrop (comp/factory Backdrop))


(defsc Modal [this {:ui/keys [modal]
                    :order/keys [ingredients] :as props}]
  {:query [:ui/id :ui/modal :order/ingredients]
   :ident (fn [] [:singleton ::modal])
   :initial-state (fn [_] {:ui/id 1
                           :ui/modal false
                           :order/ingredients (comp/get-initial-state Burger)})}
  (let [css (if modal :.show :.noShow)
        burger-data (map #(select-keys % [:ingredient :count]) (:order/ingredients ingredients))]
    (comp/fragment
      (ui-backdrop props)
      (dom/div {:classes ["Modal" css]}
        (dom/h3 "Your Order")
        (dom/p "A delicious burger with the following ingredients:")
        (dom/ul
          (map #(dom/li (str (:ingredient %) ": " (:count %))) burger-data))
        (dom/p (dom/strong "Total Price: " (.toFixed (:order/total-price ingredients) 2)))
        (dom/p "Continue to check out?")
        (dom/button {:classes ["Button Danger"] :onClick #(comp/transact! this [(toggle-modal props)]) } "CANCEL")
        (dom/button {:classes ["Button Success"] :onClick #(js/alert "Ready for checkout....")} "CONTINUE")))))

(def ui-modal (comp/factory Modal))
