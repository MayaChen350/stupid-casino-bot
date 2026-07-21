(ns mayachen350.casinobot.discord.cmds
  (:import [net.dv8tion.jda.api JDA]
           [net.dv8tion.jda.api.interactions IntegrationType InteractionContextType]
           [net.dv8tion.jda.api.interactions.commands Command$Choice OptionType]
           [net.dv8tion.jda.api.interactions.commands.build
            CommandData
            Commands
            OptionData
            SlashCommandData]
           [net.dv8tion.jda.api.requests.restaction CommandListUpdateAction]))

(defrecord Cmd [name registerer handler])

(defn new-cmd
  "Example:
  ```clojure
    (def say-cmd
        (new-cmd 
          \"say\" \"The bot will say whatever you want it to say.\" say-logic
          (with-option STRING \"msg\" \"The message to say\"))))
  ```"
  [name description cmd-handler & params]
  (->Cmd
   name
   (fn [cmds-list]
     (CommandListUpdateAction/.addCommands
      cmds-list
      (into-array
       CommandData
       [(-> (Commands/slash name description) ;; commands slash slash
            (SlashCommandData/.setContexts InteractionContextType/ALL)
            (SlashCommandData/.setIntegrationTypes IntegrationType/ALL)
            (as-> it (reduce #(%2 %1) it params)))])))
   cmd-handler))

(defn with-option
  "e.g. (with-option \"INTEGER\" \"num\" \"A number\" :required )"
  ([option-type name description]
   (with-option option-type name description nil))

  ([option-type name description required? & modifiers]
   (fn [cmds]
     (SlashCommandData/.addOptions
      cmds
      (java.util.ArrayList. [(reduce #(%2 %1)
                                     (OptionData. (OptionType/valueOf option-type)
                                                  name
                                                  description
                                                  (= :required required?))
                                     modifiers)])))))

(defn with-choices [choices]
  (fn [option-data]
    (OptionData/.addChoices option-data
                            (mapv (fn [[name value]]
                                    (Command$Choice. name (if (nil? value) name value)))
                                  choices))))

(defn handle-cmds
  [event-ctx cmds-hashtable]
  ((get cmds-hashtable (.getName event-ctx)) event-ctx))

(defn register-cmds [jda cmds]
  (as-> (JDA/.updateCommands jda) cmds-list
    (reduce #((:registerer %2) %1) cmds-list cmds)
    (.queue cmds-list)))
