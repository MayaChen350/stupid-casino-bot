(ns mayachen350.casinobot.discord.cmds
  (:import [net.dv8tion.jda.api JDA]
           [net.dv8tion.jda.api.interactions IntegrationType InteractionContextType]
           [net.dv8tion.jda.api.interactions.commands OptionType Command$Choice]
           [net.dv8tion.jda.api.interactions.commands.build
            CommandData
            Commands
            OptionData
            SlashCommandData]
           [net.dv8tion.jda.api.requests.restaction CommandListUpdateAction]))

(defn new-cmd
  "Example:
  ```clojure
    (def say-cmd
        (new-cmd 
          \"say\" \"The bot will say whatever you want it to say.\" say-logic
          (with-option STRING \"msg\" \"The message to say\"))))
  ```"
  [name description cmd-handler & params]
  [name
   (fn [cmds-list]
     (CommandListUpdateAction/.addCommands
      cmds-list
      (into-array
       CommandData
       [(-> (Commands/slash name description) ;; commands slash slash
            (SlashCommandData/.setContexts InteractionContextType/ALL)
            (SlashCommandData/.setIntegrationTypes IntegrationType/ALL)
            (as-> it (reduce #(%2 %1) it params)))]))) ;; the params are in a list 
   cmd-handler])

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
                                    (Command$Choice. name (if (nil? value) name nil)))
                                  choices))))

(defn slash-commands-handler [cmds]
  [(fn [event cmds-table] ;; cmds handlers
     ((get cmds-table (.getName event)) event))
   (fn [jda] ;; cmds register
     (let [cmds-list (JDA/.updateCommands jda)]
       (.queue
        (reduce #(%2 %1) cmds-list (mapv #(nth % 1) cmds)))))])
