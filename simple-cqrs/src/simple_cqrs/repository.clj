(ns simple-cqrs.repository
  (:require [simple-cqrs.event-store :as es]
            [simple-cqrs.aggregate-root :as ar]))

(defn save [{:keys [id] :as ar} expected-version]
  (es/save-events id (ar/uncommitted-changes ar) expected-version))

(defn by-id [id]
  (->> (es/events-for-aggregate id)
       (ar/load-from-history {})))
