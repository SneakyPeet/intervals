(ns i.app
  (:require [goog.events :as events]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [reagent.dom :as rd]
            [i.components :as c]
            [i.state :as state]
            [i.audio]))

(defn app []
  [:div.section
   [:div.container
    [c/timers]
    [c/buttons]]])


(defn mount-reagent []
  (rd/render app (js/document.getElementById "app")))


(defn ^:export run []
  (rf/dispatch-sync [::state/initialize])
  (mount-reagent))


(defn- listen []
  (events/listen js/window "load" #(run))
  (js/setInterval #(rf/dispatch [::state/tick]) 1000))

(defonce listening? (listen))


(defn ^:dev/after-load shaddow-start [] (mount-reagent))
(defn ^:dev/before-load shaddow-stop [])
