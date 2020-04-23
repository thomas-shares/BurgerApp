(ns app.component.burger.burger
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.burger.burger-ingredient :refer [ui-burger-ingredient BurgerIngredient]]))

(defsc Burger [this {:order/keys [ingredients] :as props}]
  {:query [:order/id :order/ingredients :order/total-price]
   :ident :order/id
   :initial-state {:order/id 1
                   :order/total-price 4.0
                   :order/ingredients [{:id 0 :ingredient "Bacon" :count 0 :price 0.5}
                                       {:id 1 :ingredient "Salad" :count 0 :price 0.1}
                                       {:id 2 :ingredient "Cheese" :count 0 :price 0.2}
                                       {:id 3 :ingredient "Meat" :count 1 :price 1.0}]}}
  (let [transformed-ingredients (mapcat #(into [] (repeat (:count %) %)) ingredients)]
    (dom/div {:className "Burger"}
       (ui-burger-ingredient {:ingredient "Bread-top"})
       (if (zero? (reduce + 0 (map :count ingredients)))
         (dom/div (dom/strong "Please start adding ingredients!"))
         (map ui-burger-ingredient transformed-ingredients))
       (ui-burger-ingredient {:ingredient "Bread-bottom"}))))

(def ui-burger (comp/factory Burger))




