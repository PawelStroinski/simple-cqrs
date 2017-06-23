(defproject cqrs-gui "0.1.0-SNAPSHOT"
  :description "GUI for Clojure version of https://github.com/gregoryyoung/m-r"
  :url "https://github.com/PawelStroinski/simple-cqrs"
  :license {:name "Public Domain"
            :comments "The HTML/CSS templates which can be found in the resources/public subfolder were copied from the original project."}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.0"]
                 [clj-http "3.5.0"]
                 [enlive "1.1.6"]]
  :plugins [[lein-ring "0.11.0"]]
  :ring {:handler cqrs-gui.main/app}
  :resource-paths ["resources"]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]]}})
