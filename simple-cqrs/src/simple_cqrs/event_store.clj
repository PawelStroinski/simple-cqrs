(ns simple-cqrs.event-store)

(defonce
  ^{:doc "A map of vectors. Keyed by aggregate id."}
  event-store (ref {}))

(defonce ^:private observers (atom []))

(defn- concurrency-check [expected-version current-version]
  (if (and expected-version current-version
           (not= expected-version current-version))
    (throw (ex-info "Concurrency error"
                    {:expected expected-version, :current current-version}))))

(defn- prepare-events [events current-version]
  (map #(assoc %1 :version (+ (or current-version -1) 1 %2))
       events
       (range (count events))))

(defn- notify-observers [events]
  (doseq [ev events
          agt @observers]
    (send agt (fn [f]
                (f ev)
                f))))

(defn save-events
  "Saves events after setting versions. If expected-version is truthy it should
   match against the current version, otherwise an exception is thrown."
  [aggregate-id events expected-version]
  (dosync
    (alter
      event-store
      (fn [m]
        (let [current (get m aggregate-id [])
              current-version (-> current peek :version)]
          (concurrency-check expected-version current-version)
          (let [prepared (prepare-events events current-version)
                all (apply conj current prepared)]
            (notify-observers prepared)
            (assoc m aggregate-id all)))))))

(defn events-for-aggregate
  "Returns events or throws an exception if the aggregate does not exist."
  [aggregate-id]
  (or (get @event-store aggregate-id)
      (throw (ex-info "Aggregate not found" {:id aggregate-id}))))

(defn register-observer
  "Registers f to be executed with each new event. f should catch exceptions."
  [f]
  (swap! observers conj (agent f)))
