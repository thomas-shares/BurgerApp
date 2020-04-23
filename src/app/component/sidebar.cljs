(ns app.component.sidebar
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))

(defsc SideBar [this props]
  {:query []}
  (dom/div "SideBar"))

(def ui-sidebar (comp/factory SideBar))
