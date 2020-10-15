(ns i.state
  (:require [re-frame.core :as rf]))


(def ^:private timers
  [[30 :repeat]
   [60 :repeat]
   [60 :once]
   [120 :once]])


(rf/reg-event-fx
 ::initialize
 (fn [_ _ ]
   {:db
    {:timers (->> timers
                  (map-indexed
                   (fn [i [t m]]
                     [i {:id i :time t :mode m}]))
                  (into {}))
     :times {}
     :selected-timer nil}
    :dispatch [::reset-times]}))


(rf/reg-event-db
 ::reset-times
 (fn [db _]
   (assoc db :times (->> (:timers db)
                         (map-indexed
                          (fn [i [_ {:keys [time]}]]
                            [i time]))
                         (into {})))))


(rf/reg-event-db
 ::tick
 (fn [db _]
   (if-let [id (:selected-timer db)]
     (update-in db [:times id] dec)
     db)))


(rf/reg-event-db
 ::toggle-timer
 (fn [db [_ id]]
   (let [selected-timer (:selected-timer db)
         current-time (get-in db [:times id])
         {:keys [time]} (get-in db [:timers id])
         old-config (get-in db [:timers selected-timer])
         running? (some? selected-timer)
         same? (= id selected-timer)]
     (cond
       (not running?)
       (assoc db :selected-timer id)

       same?
       (assoc-in db [:times id] time)

       (not same?)
       (-> db
           (assoc :selected-timer id)
           (assoc-in [:times selected-timer] (:time old-config)))))))


(rf/reg-event-db
 ::stop
 (fn [db _]
   (dissoc db :selected-timer)))


(rf/reg-sub
 ::timers
 (fn [db _]
   (vals (:timers db))))


(rf/reg-sub
 ::times
 (fn [db _]
   (:times db)))


(rf/reg-sub
 ::timer-time
 :<- [::times]
 (fn [times [_ id]]
   (get times id)))


(rf/reg-sub
 ::running?
 (fn [db _]
   (some? (:selected-timer db))))


(rf/reg-sub
 ::selected-timer
 (fn [db _]
   (:selected-timer db)))


(rf/reg-sub
 ::active?
 :<- [::selected-timer]
 (fn [t [_ id]]
   (= t id)))
