(ns i.components
  (:require [re-frame.core :as rf]
            [i.state :as state]))


(defn- time-box [{:keys [id time mode]}]
  (let [current-time @(rf/subscribe [::state/timer-time id])
        active? @(rf/subscribe [::state/active? id])]
    [:div.notification.has-text-centered.pl-0.pr-0
     {:class (if active?
               (case mode
                 :repeat "is-danger"
                 :once "is-success"
                 "")
               (case mode
                 :repeat "has-text-danger"
                 :once "has-text-success"
                 ""))
      :style  {:cursor "pointer"
               ;:margin "0.5rem"
               :width "45%"
               :height "20vh"
               :display "flex"
               :align-items "center"}
      :on-click #(rf/dispatch [::state/toggle-timer id])}
     [:div {:style {:width "100%"}}
      [:p.is-size-4 current-time]
      [:small.heading mode]]]))


(defn timers []
  (let [timers @(rf/subscribe [::state/timers])]
    [:div {:style {:display "flex"
                   :flex-wrap "wrap"
                   :justify-content "space-between"}}
     (->> timers
          (map-indexed
           (fn [j t]
             [:<> {:key j} [time-box t]])))]))


(defn buttons []
  (let [running? @(rf/subscribe [::state/running?])]
    [:div.level.is-mobile.mt-5
     [:div.level-item
      [:button.button.is-large.is-success.is-light
       {:on-click #(rf/dispatch [::state/reset-times])}
       "Reset"]]
     (when running?
       [:div.level-item
        [:button.button.is-large.is-danger.is-light
         {:on-click #(rf/dispatch [::state/stop])}
         "Stop"]])]))
