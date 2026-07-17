(ns mayachen350.casinobot.core
  (:require [mayachen350.casinobot.discord.cmds :refer [slash-commands-handler]]
            [mayachen350.casinobot.configs :refer [cmds]])
  (:import [io.github.cdimascio.dotenv Dotenv]
           [java.util EnumSet]
           [net.dv8tion.jda.api JDABuilder]
           [net.dv8tion.jda.api.hooks ListenerAdapter]
           [net.dv8tion.jda.api.requests GatewayIntent])
  (:gen-class))

(def intents
  (EnumSet/of
   GatewayIntent/GUILD_MESSAGES
   GatewayIntent/GUILD_MESSAGE_REACTIONS
   GatewayIntent/MESSAGE_CONTENT))

(def token
  (-> (Dotenv/load)
      (.get "DISCORD_TOKEN")))

(def handler-result (slash-commands-handler cmds))

(def handle-cmds (first handler-result))
(def register-cmds (second handler-result))

(def cmds-table
  (apply hash-map
         (mapcat #(vector (first %) (last %)) cmds)))

(def listener
  (proxy [ListenerAdapter] []
    (onSlashCommandInteraction [event]
      (handle-cmds event cmds-table))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->
   (JDABuilder/createLight token intents)
   (JDABuilder/.addEventListeners (into-array Object [listener]))
   (JDABuilder/.build)
   (register-cmds)))

