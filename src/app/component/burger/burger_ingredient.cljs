(ns app.component.burger.burger-ingredient
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]))

(defsc BurgerIngredient [this props]
  (condp = (:ingredient props)
    "Bread-top" (dom/div {:className "BreadTop"}
                  (dom/div {:className "Seeds1"})
                  (dom/div {:className "Seed2"}))
    "Salad" (dom/div {:className "Salad"})
    "Bacon" (dom/div {:className "Bacon"})
    "Cheese" (dom/div {:className "Cheese"})
    "Meat" (dom/div {:className "Meat"})
    "Bread-bottom" (dom/div {:className "BreadBottom"})))

(def ui-burger-ingredient (comp/factory BurgerIngredient))

