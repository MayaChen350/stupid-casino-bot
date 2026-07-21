(ns mayachen350.casinobot.configs
  (:require
   [mayachen350.casinobot.gambling.color-wheel.core :refer [color-wheel-cmd]])
  (:import
   [io.github.cdimascio.dotenv Dotenv]
   [java.util EnumSet]
   [net.dv8tion.jda.api.requests GatewayIntent]))

(def intents
  (EnumSet/of
   GatewayIntent/GUILD_MESSAGES
   GatewayIntent/GUILD_MESSAGE_REACTIONS
   GatewayIntent/MESSAGE_CONTENT))

(def token
  (-> (Dotenv/load)
      (.get "DISCORD_TOKEN")))

(def cmds [color-wheel-cmd])
