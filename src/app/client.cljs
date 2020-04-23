(ns app.client
  (:require
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr :refer [defrouter]]
    [com.fulcrologic.fulcro.dom :as dom]
    [com.fulcrologic.fulcro.data-fetch :as df]
    [app.app :refer [app]]
    [app.routing :as routing]
    [app.component.main :refer [ui-main Main]]
    [app.component.sidebar :refer [ui-sidebar SideBar]]
    [app.component.toolbar :refer [ui-toolbar ToolBar]]
    [app.component.checkout :refer [ui-checkout Checkout]]
    [com.fulcrologic.fulcro.algorithms.denormalize :as fdn]))

(defrouter TopRouter [this {:keys [current-state route-props] :as props}]
  {:router-targets [Main Checkout]}
  (ui-main route-props))

(def ui-top-router (comp/factory TopRouter))

(defsc Layout [this {:root/keys [router] :as props}]
  {:query [{:root/router (comp/get-query TopRouter)}]
   :initial-state {:root/router {}}}
  (dom/div {:className "Content"}
    (ui-top-router router)
    (ui-toolbar)))

(defn ^:export init
  "Shadow-cljs sets this up to be our entry-point function. See shadow-cljs.edn `:init-fn` in the modules of the main build."
  []
  (app/mount! app Layout "app")
  (dr/initialize! app)
  (routing/start!)
  (js/console.log "Loaded"))

(defn ^:export refresh
  "During development, shadow-cljs will call this on every hot reload of source. See shadow-cljs.edn"
  []
  ;; re-mounting will cause forced UI refresh, update internals, etc.
  (app/mount! app Layout "app")
  (js/console.log "Hot reload"))


(comment
  (fdn/db->tree
    (comp/get-query Layout {})
    (app/current-state app)
    (app/current-state app)))



