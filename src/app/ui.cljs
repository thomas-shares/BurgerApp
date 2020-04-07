(ns app.ui
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))



(defsc Backdrop [this {:ui/keys [modal?]}]
  {}
  (when modal?
    (dom/div {:className "Backdrop" :onClick #(js/alert "backdrop")})))

(def ui-backdrop (comp/factory Backdrop))


(defsc Modal [this {:ui/keys [modal?]
                    :order/keys [burger] :as props}]
  {:query [:ui/id :ui/modal? :order/burger]
   :ident (fn [] [:singleton ::modal])
   :initial-state {:ui/id 1
                   :ui/modal? false}}
  (js/console.log "Modal: " modal?)

  (let [css (if modal? :.show :.noShow)]

    (dom/div {:classes ["Modal" css]}
      (ui-backdrop props))))


(def ui-modal (comp/factory Modal))
