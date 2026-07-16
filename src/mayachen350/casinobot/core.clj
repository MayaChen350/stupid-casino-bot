(ns mayachen350.casinobot.core
  (:require [mayachen350.casinobot.discord.cmds :refer [slash-commands-handler]]
            [mayachen350.casinobot.gambling.color-wheel :refer [color-wheel-cmd]])
  (:import [io.github.cdimascio.dotenv Dotenv]
           [java.util EnumSet]
           [net.dv8tion.jda.api JDABuilder]
           [net.dv8tion.jda.api.requests GatewayIntent]
           [net.dv8tion.jda.api.hooks ListenerAdapter])
  (:gen-class))

(def intents
  (EnumSet/of
   GatewayIntent/GUILD_MESSAGES
   GatewayIntent/GUILD_MESSAGE_REACTIONS
   GatewayIntent/MESSAGE_CONTENT))

(def token
  (-> (Dotenv/load)
      (.get "DISCORD_TOKEN")))

(def cmds [color-wheel-cmd])

(def handler-result (slash-commands-handler cmds))

(def handle-cmds (first handler-result))
(def register-cmds (second handler-result))

(def listener
  (proxy [ListenerAdapter] []
    (onSlashCommandInteraction [event]
      (handle-cmds event))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->
    (JDABuilder/createLight token intents)
    (JDABuilder/.addEventListeners (into-array Object [listener]))
    (JDABuilder/.build)
    (register-cmds)))
