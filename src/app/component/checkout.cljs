(ns app.component.checkout
  (:require [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
            [com.fulcrologic.fulcro.routing.dynamic-routing :as dr :refer [defrouter]]
            [com.fulcrologic.fulcro.dom :as dom]
            [app.component.burger.burger :refer [Burger ui-burger]]
            [com.fulcrologic.fulcro.mutations :as m]
            [com.fulcrologic.fulcro.algorithms.form-state :as fs]
            [com.fulcrologic.fulcro.algorithms.form-state :as form-state]
            [cljs.spec.alpha :as s]
            [clojure.string :as str]))

(def not-empty? #(not (empty? (clojure.string/trim %))))
(def email-re #"^(([^<>()\[\]\\.,;:\s@\"]+(\.[^<>()\[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$")
(s/def :contact-details/email #(re-matches email-re %))
(s/def :contact-details/country (s/and string? #(seq (str/trim %))))
(s/def :contact-details/zip-code #(and (= 5 (count %))
                                       (not (empty? (re-matches #"^\d+$" %)))))
(s/def :contact-details/street (s/and string? #(seq (str/trim %))))
(s/def :contact-details/name (s/and string? #(seq (str/trim %))))

(def order-form [{:name "name"
                  :db-id :contact-details/name
                  :element-type :input
                  :element-config {:type "text"
                                   :placeholder "Your Name"}}
                 {:name "Street"
                  :db-id :contact-details/street
                  :element-type :input
                  :element-config {:type "text"
                                   :placeholder "Street"}}
                 {:name "zip code"
                  :db-id :contact-details/zip-code
                  :element-type :input
                  :element-config {:type "numeric"
                                   :placeholder "ZIP Code"}}
                 {:name "country"
                  :db-id :contact-details/country
                  :element-type :input
                  :element-config {:type "text"
                                   :placeholder "Country"}}
                 {:name "email"
                  :db-id :contact-details/email
                  :element-type :input
                  :element-config {:type "email"
                                   :placeholder "Your E-Mail"}}
                 {:name "delivery-method"
                  :db-id :contact-details/delivery-method
                  :element-type :select
                  :element-config {:options [{:value "fastest" :text "Fastest"}
                                             {:value "cheapest" :text "Cheapest"}]}
                  :value "fastest"}])

(defn select-option [{:keys [value text]}]
  (dom/option {:value value} text))

(defn form-input [this props {:keys [name element-type db-id element-config] :as m}]
  (let [dirty?   (fs/dirty? db-id)
        invalid? (= :invalid (fs/get-spec-validity db-id))]
    (js/console.log invalid? dirty? db-id (fs/get-spec-validity this  db-id) (fs/get-spec-validity db-id))
    (dom/div {:className "InputContactDetails"}
      (dom/label {:className "Label"})
      (case element-type
        :input (dom/input {:type (:type element-config)
                           :classes ["InputElement" (when invalid? "Invalid")]
                           :value (db-id props)
                           :placeholder (:placeholder element-config)
                           :onChange #(m/set-string! this db-id :event %)})
        :select (dom/select {:name name
                             :className "InputElement"
                             :onChange (fn [evt]
                                         (when-let [v (some-> (.-target evt) (.-value))]
                                           (m/set-value! this db-id v)))}
                  (map #(select-option %) (:options element-config)))
        (dom/div "ERROR, unknown element-type")))))

(defn validate-form [props]
  #_(every? true?
      (map
        (fn [{:keys [validation db-id]}] (validation (db-id props)))
        (map #(select-keys % [:validation :db-id]) order-form)))
  false)

(defsc ContactDetails [this props]
  {:query [:id :contact-details/name :contact-details/street :contact-details/zip-code
           :contact-details/country :contact-details/email :contact-details/delivery-method fs/form-config-join]
   :ident [:contact-details/id :id]
   :form-fields #{:contact-details/name :contact-details/street :contact-details/zip-code
                  :contact-details/country :contact-details/email :contact-details/delivery-method}
   :initial-state {:contact-details/name ""
                   :contact-details/street ""
                   :contact-details/zip-code ""
                   :contact-details/country ""
                   :contact-details/email ""
                   :contact-details/delivery-method "fastest"}
   :route-segment ["checkout" "contact-details"]
   :pre-merge (fn [{:keys [data-tree]}] (form-state/add-form-config ContactDetails data-tree))}
  (let [enabled? (validate-form props)]
    (dom/div {:className "ContactData"}
      (dom/h4 "Enter your contact data")
      (dom/form
        (map #(form-input this props %) order-form)
        (dom/button {:classes ["Button Success"]
                     :disabled (not enabled?)
                     :onClick #()} "Order")))))

(def ui-contact-details (comp/factory ContactDetails))

(defsc Checkout [this {:order/keys [burger]:as props}]
  {:query [{:order/burger (comp/get-query Burger)}]
   :ident (fn [] [:singleton ::checkout])
   :initial-state (fn [_] {:order/burger (comp/get-initial-state Burger)})
   :route-segment ["checkout"]}
  (dom/div {:className "CheckoutSummary"}
    (dom/h1 "We hope it tastes well!!!")
    (dom/div {:style {:width "100%" :margin :auto}}
      (ui-burger burger))
    (dom/a {:href "/"}
      (dom/button {:classes ["Button Danger"]} "CANCEL"))
    (dom/a {:href "/checkout/contact-details"}
      (dom/button {:classes ["Button Success"]} "CONTINUE"))))

(def ui-checkout (comp/factory Checkout))
