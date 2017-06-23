(ns simple-cqrs.read-model)

(defmulti query :query)
