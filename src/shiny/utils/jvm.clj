(ns shiny.utils.jvm
  (:import (java.lang.management ManagementFactory)))

(defn get-input-arguments
  []
  (.getInputArguments (ManagementFactory/getRuntimeMXBean)))
