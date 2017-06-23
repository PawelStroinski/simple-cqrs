(ns cqrs-gui.main
  (:require [compojure.core :refer :all]
            [compojure.coercions :refer [as-int]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as response]
            [cqrs-gui.client :as client]
            [cqrs-gui.views :as views])
  (:import (java.util UUID)))

(defn- command [command]
  (client/command command)
  (response/redirect "/"))

(defn- query [query snippet & [title]]
  (->> (when query (client/query query))
       (snippet)
       (views/site title)))

(defroutes
  app-routes
  (GET "/" []
    (query {:query :get-inventory-items} views/index "Home Page"))
  (GET "/details/:id" [id]
    (query {:query :get-inventory-item-details, :id id} views/details))
  (GET "/add" []
    (query nil views/add))
  (POST "/add" [name]
    (command {:command           :create-inventory-item
              :inventory-item-id (str (UUID/randomUUID))
              :name              name}))
  (GET "/change-name/:id" [id]
    (query {:query :get-inventory-item-details, :id id} views/change-name))
  (POST "/change-name" [id name version :<< as-int]
    (command {:command           :rename-inventory-item
              :inventory-item-id id
              :new-name          name
              :original-version  version}))
  (GET "/deactivate/:id" [id]
    (query {:query :get-inventory-item-details, :id id} views/deactivate))
  (POST "/deactivate" [id version :<< as-int]
    (command {:command           :deactivate-inventory-item
              :inventory-item-id id
              :original-version  version}))
  (GET "/check-in/:id" [id]
    (query {:query :get-inventory-item-details, :id id} views/check-in))
  (POST "/check-in" [id number :<< as-int version :<< as-int]
    (command {:command           :check-in-items-to-inventory
              :inventory-item-id id
              :count             number
              :original-version  version}))
  (GET "/remove/:id" [id]
    (query {:query :get-inventory-item-details, :id id} views/remove))
  (POST "/remove" [id number :<< as-int version :<< as-int]
    (command {:command           :remove-items-from-inventory
              :inventory-item-id id
              :count             number
              :original-version  version})))

(defn- wrap-error-naïvely [handler]
  (fn [request]
    (try
      (handler request)
      (catch Throwable e
        (throw (if-let [msg (-> e (ex-data) :body)] (Exception. msg) e))))))

(def app
  (-> app-routes
      (wrap-error-naïvely)
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))))
