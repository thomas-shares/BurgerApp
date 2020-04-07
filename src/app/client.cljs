(ns app.client
  (:require
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.rendering.keyframe-render2 :refer [render!]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.data-fetch :as df]
    [app.component.main :refer [ui-main Main]]
    [app.component.sidebar :refer [ui-sidebar SideBar]]
    [app.component.toolbar :refer [ui-toolbar ToolBar]]))

(defsc Layout [this {:root/keys [main] :as props}]
  {:query [{:root/toolbar (comp/get-query ToolBar)}
           ;{:root/sidebar (comp/get-query SideBar)
           {:root/main (comp/get-query Main)}]
   :initial-state (fn (_) {:root/toolbar (comp/get-initial-state ToolBar)}
                          ;{:root/sidebar (comp/get-initial-state SideBar)}
                          {:root/main (comp/get-initial-state Main)})}
  (dom/div {:className "Content"}
    (ui-toolbar)
    ;(ui-sidebar)
    (ui-main main)))

(defonce app (app/fulcro-app {:optimized-render! render!}))

(defn ^:export init
  "Shadow-cljs sets this up to be our entry-point function. See shadow-cljs.edn `:init-fn` in the modules of the main build."
  []
  (app/mount! app Layout "app")
  (js/console.log "Loaded"))

(defn ^:export refresh
  "During development, shadow-cljs will call this on every hot reload of source. See shadow-cljs.edn"
  []
  ;; re-mounting will cause forced UI refresh, update internals, etc.
  (app/mount! app Layout "app")
  (js/console.log "Hot reload"))
