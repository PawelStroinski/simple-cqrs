(ns simple-cqrs.main
  (:require [compojure.core :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.util.response :refer [response status]]
            [simple-cqrs.command-handlers :as c]
            [simple-cqrs.read-model
             inventory-item-detail-view inventory-list-view] ; register stuff
            [simple-cqrs.read-model :as rm]))

(defroutes
  app-routes
  (POST "/command" {:keys [body-params]}
    (do (c/handle body-params)
        (response "")))
  (GET "/query" {:keys [body-params]}
    (response (rm/query body-params))))

(defn- wrap-error-naïvely [handler]
  (fn [request]
    (try
      (handler request)
      (catch Throwable e
        (-> e (str) (response) (status 400))))))

(def app
  (-> app-routes
      (wrap-restful-format)
      (wrap-error-naïvely)
      (wrap-defaults api-defaults)))
