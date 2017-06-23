(ns cqrs-gui.client
  (:require [clj-http.client :as client]))

(def ^:private root "http://localhost:3000")

(defn- request [method path body]
  (-> {:url          (str root path)
       :method       method
       :body         (pr-str body)
       :content-type :edn
       :accept       :edn
       :as           :auto}
      (client/request)
      :body))

(defn command [command]
  (request :post "/command" command))

(defn query [query]
  (request :get "/query" query))
