(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.Burger.build-controls :refer [toggle-modal]]))



(defsc Backdrop [this {:ui/keys [modal] :as props}]
  {}
  (when (:ui/modal modal)
    (dom/div {:className "Backdrop" :onClick
                         #(comp/transact! this [(toggle-modal props)])} "Less")))

(def ui-backdrop (comp/factory Backdrop))


(defsc Modal [this {:ui/keys [modal]
                    :order/keys [burger] :as props}]
  {:query [:ui/id :ui/modal :order/burger]
   :ident (fn [] [:singleton ::modal])
   :initial-state {:ui/id 1
                   :ui/modal false}}
  (let [css (if (:ui/modal modal) :.show :.noShow)
        burger-data (map #(select-keys % [:ingredient :count]) (:order/ingredients burger))]
    (comp/fragment
      (ui-backdrop props)
      (dom/div {:classes ["Modal" css]}
        (dom/h3 "Your Order")
        (dom/p "A delicious burger with the following ingredients:")
        (dom/ul
          (map #(dom/li (str (:ingredient %) ": " (:count %))) burger-data))
        (dom/p (dom/strong "Total Price: " (.toFixed (:order/total-price burger) 2)))
        (dom/p "Continue to check out?")
        (dom/button {:classes ["Button Danger"] :onClick #(comp/transact! this [(toggle-modal props)]) } "CANCEL")
        (dom/button {:classes ["Button Success"] :onClick #(js/alert "Ready for checkout....")} "CONTINUE")))))

(def ui-modal (comp/factory Modal))
