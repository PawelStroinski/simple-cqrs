(defproject simple-cqrs "0.1.0-SNAPSHOT"
  :description "Clojure version of https://github.com/gregoryyoung/m-r"
  :url "https://github.com/PawelStroinski/simple-cqrs"
  :license {:name "Public Domain"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.0"]
                 [ring-middleware-format "0.7.2"]]
  :plugins [[lein-ring "0.11.0"]]
  :ring {:handler simple-cqrs.main/app, :nrepl {:start? true, :port 59685}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]]}})
