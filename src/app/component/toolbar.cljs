(ns app.component.toolbar
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))

(defsc ToolBar [this props]
  {:query []}
  (dom/header {:className "ToolBar"}
    (dom/div {:className "Logo"}
      (dom/div {:className "Logo"}
        (dom/img {:src "burger-logo.png"})))
    (dom/nav {:className "DesktopOnly"}
      (dom/ul {:className "NavigationItems"}
        (dom/li {:className "NavtigationItem"}
          (dom/a {:href "/"} "Burger Builder"))
        (dom/li {:className "NavtigationItem"}
          (dom/a {:href "/orders"} "Orders"))))))

(def ui-toolbar (comp/factory ToolBar))
