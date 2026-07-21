(ns mayachen350.casinobot.core
  (:require [mayachen350.casinobot.configs :refer [cmds intents token]]
            [mayachen350.casinobot.discord.cmds :refer [handle-cmds
                                                        register-cmds]])
  (:import [net.dv8tion.jda.api JDABuilder]
           [net.dv8tion.jda.api.hooks ListenerAdapter])
  (:gen-class))

(def cmds-table
  (apply hash-map
         (mapcat (fn [{:keys [name handler]}] [name handler])
                 cmds)))

(def listener
  (proxy [ListenerAdapter] []
    (onSlashCommandInteraction [event]
      (handle-cmds event cmds-table))))

(defn -main [& args]
  (-> (JDABuilder/createLight token intents)
      (JDABuilder/.addEventListeners (into-array Object [listener]))
      (JDABuilder/.build)
      (register-cmds cmds)))

