(ns simple-cqrs.read-model.inventory-list-view
  (:require [simple-cqrs.inventory-item :as ii]
            [simple-cqrs.event-store :as es]
            [simple-cqrs.read-model :as rm]))

(defonce ^:private db (atom {}))

(defmulti ^:private handle :event)

(defonce ^:private observer (es/register-observer handle))

(defmethod handle :default [_])

(defmethod handle ::ii/created
  [{:keys [id name]}]
  (swap! db assoc id {:id id, :name name}))

(defmethod handle ::ii/renamed
  [{:keys [id new-name]}]
  (swap! db assoc-in [id :name] new-name))

(defmethod handle ::ii/deactivated
  [{:keys [id]}]
  (swap! db dissoc id))

(defmethod rm/query :get-inventory-items [_]
  (vals @db))
