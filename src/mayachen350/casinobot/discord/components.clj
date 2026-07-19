(ns mayachen350.casinobot.discord.components
  (:require [clojure.java.io :as io])
  (:import [net.dv8tion.jda.api.components.container Container ContainerChildComponent]
           [net.dv8tion.jda.api.components.mediagallery MediaGallery MediaGalleryItem]
           [net.dv8tion.jda.api.components.textdisplay TextDisplay]
           [net.dv8tion.jda.api.utils FileUpload]))

(defn new-container [sub-component & sub-components]
  (Container/of sub-component (into-array ContainerChildComponent sub-components)))

(defn new-textdisplay [text]
  (TextDisplay/of text))

(defn new-res-upload [res-name]
  (FileUpload/fromData
   (io/input-stream (str "resources/" res-name))
   (str (hash res-name) ".png")))

(defn with-media
  "The type key is either :url or :res. The function can have an optional last parameter, usually :spoiler."
  ([type-key media-name]
   (case type-key
     :url (MediaGalleryItem/fromUrl media-name)
     :res  (MediaGalleryItem/fromFile
            (new-res-upload media-name))))

  ([type-key media-name _]
   (.withSpoiler (with-media type-key media-name))))

(def new-media-item with-media) ;; alias for when this name makes more sense

(defn new-media-gallery [media-upload & media-uploads]
  (MediaGallery/of media-upload (into-array MediaGalleryItem media-uploads)))
