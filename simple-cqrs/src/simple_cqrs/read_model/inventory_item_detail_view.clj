(ns simple-cqrs.read-model.inventory-item-detail-view
  (:require [simple-cqrs.inventory-item :as ii]
            [simple-cqrs.event-store :as es]
            [simple-cqrs.read-model :as rm]))

(defonce ^:private db (atom {}))

(defmulti ^:private handle :event)

(defmethod handle :default [_])

(defmethod handle ::ii/created
  [{:keys [id name]}]
  (swap! db assoc id {:id id, :name name, :current-count 0, :version 0}))

(defmethod handle ::ii/renamed
  [{:keys [id new-name version]}]
  {:pre [(@db id)]}
  (swap! db #(-> (assoc-in % [id :name] new-name)
                 (assoc-in [id :version] version))))

(defmethod handle ::ii/items-removed
  [{:keys [id count version]}]
  {:pre [(@db id)]}
  (swap! db #(-> (update-in % [id :current-count] - count)
                 (assoc-in [id :version] version))))

(defmethod handle ::ii/items-checked-in
  [{:keys [id count version]}]
  {:pre [(@db id)]}
  (swap! db #(-> (update-in % [id :current-count] + count)
                 (assoc-in [id :version] version))))

(defmethod handle ::ii/deactivated
  [{:keys [id]}]
  (swap! db dissoc id))

(defonce ^:private observer (es/register-observer handle))

(defmethod rm/query :get-inventory-item-details [{:keys [id]}]
  (@db id))
