(ns simple-cqrs.inventory-item
  (:require [simple-cqrs.aggregate-root :refer [apply* apply-change]]))

(defmethod apply* ::created [ii {:keys [id]}]
  (assoc ii :id id, :activated true))

(defmethod apply* ::deactivated [ii _]
  (assoc ii :activated false))

(defn make [id name]
  (apply-change {} {:event ::created, :id id, :name name}))

(defn deactivate [{:keys [activated id] :as ii}]
  (when-not activated
    (throw (ex-info "Already deactivated" ii)))
  (apply-change ii {:event ::deactivated, :id id}))

(defn remove-count [{:keys [id] :as ii} count]
  (when-not (pos? count)
    (throw (ex-info "Can't remove nonpositive count from inventory" ii)))
  (apply-change ii {:event ::items-removed, :id id, :count count}))

(defn check-in [{:keys [id] :as ii} count]
  (when-not (pos? count)
    (throw (ex-info "Must have a count greater than 0 to add to inventory" ii)))
  (apply-change ii {:event ::items-checked-in, :id id, :count count}))

(defn change-name [{:keys [id] :as ii} new-name]
  (when-not (seq new-name)
    (throw (IllegalArgumentException. "new-name")))
  (apply-change ii {:event ::renamed, :id id, :new-name new-name}))
