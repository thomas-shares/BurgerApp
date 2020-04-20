(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
            [app.component.burger.build-controls :refer [toggle-modal]]
            [app.component.burger.burger :refer [Burger]]))



(defsc Backdrop [this {:ui/keys [modal] :as props}]
  {}
  (when modal
    (dom/div {:className "Backdrop" :onClick
                         #(comp/transact! this [(toggle-modal props)])})))

(def ui-backdrop (comp/factory Backdrop))


(defsc Modal [this {:ui/keys [modal burger]
                     :as props}]
  {:query [:ui/id :ui/modal :ui/burger]
   :ident (fn [] [:singleton ::modal])
   :initial-state (fn [_] {:ui/id 1
                           :ui/modal false
                           :ui/burger (comp/get-initial-state Burger)})}
  (js/console.log "ingre: " props)
  (let [ingredients burger

        css (if modal :.show :.noShow)
        burger-data (map #(select-keys % [:ingredient :count]) (:order/ingredients ingredients))]
    (comp/fragment
      (ui-backdrop props)
      (dom/div {:classes ["Modal" css]}
        (dom/h3 "Your Order")
        (dom/p "A delicious burger with the following ingredients:")
        (dom/ul
          (map #(dom/li (str (:ingredient %) ": " (:count %))) burger-data))
        ;;(dom/p (dom/strong "Total Price: " (.toFixed (:order/total-price ingredients) 2)))
        (dom/p "Continue to check out?")
        (dom/button {:classes ["Button Danger"]
                     :onClick #(comp/transact! this [(toggle-modal props)]) } "CANCEL")
        (dom/a {:href "checkout"}
          (dom/button {:classes ["Button Success"]
                           :onClick #(;(dr/change-route this ["checkout"])
                                      (comp/transact! this [(toggle-modal props)]))} "CONTINUE"))))))

(def ui-modal (comp/factory Modal))
