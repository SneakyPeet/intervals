(ns i.app
  (:require [goog.events :as events]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [reagent.dom :as rd]
            [i.components :as c]
            [i.state :as state]
            [i.audio]))

(def v 5)

(defn- version []
  [:span {:style {:position "fixed"
                  :right "1rem"
                  :bottom "1rem"}}
   v])

(defn app []
  [:div.section.has-background-dark {:style {:height "100vh"}}
   [:div.container
    [version]
    [c/timers]
    [c/buttons]]])


(defn mount-reagent []
  (rd/render app (js/document.getElementById "app")))


(defn ^:export run []
  (rf/dispatch-sync [::state/initialize])
  (mount-reagent))


(defn- wake-lock []
  (when (and js/navigator js/navigator.wakeLock)
    (-> (js/navigator.wakeLock.request "screen")
        (.then #(prn "woke"))
        (.catch #(js/alert "wakelock not available")))))


(defn- listen []
  (events/listen js/window "load" #(run))
  (js/setInterval #(rf/dispatch [::state/tick]) 1000)
  (wake-lock))

(defonce listening? (listen))


(defn ^:dev/after-load shaddow-start [] (mount-reagent))
(defn ^:dev/before-load shaddow-stop [])
