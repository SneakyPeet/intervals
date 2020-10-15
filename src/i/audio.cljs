(ns i.audio
  (:require [re-frame.core :as rf]))


(def sounds {:beep (js/Audio. "/audio/beep.mp3")})

(defn play [s]
  (if-let [audio (get sounds s)]
    (.play audio)
    (prn "No sound file for " s)))


(rf/reg-fx
 :play-sound
 (fn [s]
   (play s)))
