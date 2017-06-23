(ns simple-cqrs.aggregate-root)

(defn uncommitted-changes [ar]
  (::changes ar))

(defn mark-changes-as-committed [ar]
  (dissoc ar ::changes))

(defmulti apply* (fn [_ m] (:event m)))

(defmethod apply* :default [ar _] ar)

(defn apply-change [ar event]
  (-> (apply* ar event)
      (update ::changes (fnil conj []) event)))

(defn load-from-history [ar events]
  (reduce apply* ar events))
