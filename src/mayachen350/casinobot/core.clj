(ns mayachen350.casinobot.core
  (:import [net.dv8tion.jda.api JDABuilder]
           [java.util EnumSet]
           [net.dv8tion.jda.api.requests GatewayIntent]
           [io.github.cdimascio.dotenv Dotenv])
  (:gen-class))

(def intents 
  (EnumSet/of
   GatewayIntent/GUILD_MESSAGES
    GatewayIntent/GUILD_MESSAGE_REACTIONS
    GatewayIntent/MESSAGE_CONTENT))

(def token
  (-> (Dotenv/load)
      (.get "DISCORD_TOKEN")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->
   (JDABuilder/createLight token intents)
   (.build)))
