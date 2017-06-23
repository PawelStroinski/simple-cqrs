(ns cqrs-gui.views
  (:refer-clojure :exclude [remove])
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.io :as io]))

(defn- resource [name]
  (io/resource (format "public/%s.html" name)))

(defn- replace-item-vars [{:keys [id name current-count version]}]
  (html/replace-vars {:id            (or id "")
                      :name          (or name "")
                      :current-count (str (or current-count ""))
                      :version       (str (or version ""))}))

(html/deftemplate
  site (resource "site")
  [title main-content]
  [:title] (html/content title)
  [:#main-content] (html/substitute main-content))

(html/defsnippet
  index (resource "index") [html/root]
  [inventory-items]
  [:li] (html/clone-for
          [item inventory-items]
          [html/any-node] (replace-item-vars item)))

(defmacro defsnippets [& syms]
  `(do ~@(for [sym syms]
           `(html/defsnippet
              ~sym (resource ~(str sym)) [html/root]
              [item#]
              [html/any-node] (replace-item-vars item#)))))

(defsnippets details add change-name deactivate check-in remove)
