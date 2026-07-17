(ns mayachen350.casinobot.gambling.color-wheel
  (:require
   [mayachen350.casinobot.discord.cmds :refer [new-cmd with-option]]
   [mayachen350.casinobot.shared :refer [in-range]])
  (:import
   [net.dv8tion.jda.api.interactions.callbacks IReplyCallback]
   [net.dv8tion.jda.api.interactions.commands CommandInteractionPayload]))

(def tier-1-colors
  {:warm [300 120]
   :cold [120 300]})

(def color-ranges
  (merge
   tier-1-colors))

(defn random-hue []
  (+ 1 (rand-int 360)))

(defn fix-color-match [hue start end]
  (if (> start end)
    (if (< hue end)
      [(+ hue 360) start (+ end 360)] ;; e.g. [0 300 10] -> [360 300 370]
      [hue start (+ end 360)]) ;; e.g. [301 300 10] -> [301 300 370]
    [hue start end]))

(defn match-color? [color-range hue]
  (let [[start end] color-range]
    (apply in-range (fix-color-match hue start end))))

(defn match-random-color? [color-range-key]
  (match-color? (get color-ranges color-range-key) (random-hue)))

(defn color-wheel-cmd-handler [event]
  (println "Color range function triggered" "with" (CommandInteractionPayload/.getOption event "thing"))
  (let [color (keyword (->
                        (CommandInteractionPayload/.getOption event "thing")
                        (.getAsString)))]
    (.queue (IReplyCallback/.reply event (str (match-random-color? color))))))

(def color-wheel-cmd
  (new-cmd
   "color_wheel" "Bet on the color wheel." color-wheel-cmd-handler
   (with-option "STRING" "thing" "uhm the thing" :required)))
