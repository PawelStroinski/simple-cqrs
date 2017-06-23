(ns simple-cqrs.command-handlers
  (:require [simple-cqrs.inventory-item :as ii]
            [simple-cqrs.repository :as r]))

(defmulti handle :command)

(defmethod handle :create-inventory-item
  [{:keys [inventory-item-id name]}]
  (-> (ii/make inventory-item-id name)
      (r/save nil)))

(defmethod handle :deactivate-inventory-item
  [{:keys [inventory-item-id original-version]}]
  (-> (r/by-id inventory-item-id)
      (ii/deactivate)
      (r/save original-version)))

(defmethod handle :remove-items-from-inventory
  [{:keys [inventory-item-id count original-version]}]
  (-> (r/by-id inventory-item-id)
      (ii/remove-count count)
      (r/save original-version)))

(defmethod handle :check-in-items-to-inventory
  [{:keys [inventory-item-id count original-version]}]
  (-> (r/by-id inventory-item-id)
      (ii/check-in count)
      (r/save original-version)))

(defmethod handle :rename-inventory-item
  [{:keys [inventory-item-id new-name original-version]}]
  (-> (r/by-id inventory-item-id)
      (ii/change-name new-name)
      (r/save original-version)))
