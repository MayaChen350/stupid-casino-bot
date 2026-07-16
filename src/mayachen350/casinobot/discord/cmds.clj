(ns mayachen350.casinobot.discord.cmds
  (:import [net.dv8tion.jda.api.interactions.commands.build Commands]
           [net.dv8tion.jda.api.interactions.commands OptionType]
           [net.dv8tion.jda.api.interactions.commands.build OptionData]
           [net.dv8tion.jda.api.interactions.commands.build CommandData]
           [net.dv8tion.jda.api.interactions InteractionContextType]
           [net.dv8tion.jda.api.interactions IntegrationType]
           [net.dv8tion.jda.api JDA]
           [net.dv8tion.jda.api.requests.restaction CommandListUpdateAction]
           [net.dv8tion.jda.api.interactions.commands.build SlashCommandData]))

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
   #(with-option % option-type name description nil))

  ([slash-cmd option-type name description required?]
   (SlashCommandData/.addOption slash-cmd
                                (OptionType/valueOf option-type)
                                name
                                description
                                (= :required required?))))

(defmacro make-cases [cmds]
  `(mapcat #(let [cmd-name# (first %)
                  handler# (last %)]
              [cmd-name# (partial handler#)])
           ~cmds))

(defn slash-commands-handler [cmds]
  [(fn [event] ;; cmds handlers
     ((case (.getName event) (make-cases cmds))
      event))
   (fn [jda] ;; cmds register
     (let [cmds-list (JDA/.updateCommands jda)]
       (.queue
        (reduce #(%2 %1) cmds-list (mapv #(nth % 1) cmds)))))])
