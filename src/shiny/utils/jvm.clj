(ns shiny.utils.jvm
  (:import (java.lang.management ManagementFactory))
  (:import (java.io IOException RandomAccessFile File))
  (:import (com.carrotsearch.sizeof RamUsageEstimator))
  (:import (org.github.jamm MemoryMeter))
  (:import (com.sun.tools.attach VirtualMachine)))

(defn get-input-arguments
  []
  (.getInputArguments (ManagementFactory/getRuntimeMXBean)))

(defn gc
  []
  (System/gc))

(defn gc*
  []
  (.gc (Runtime/getRuntime)))

;;; Run this to see what your JRE's open file limit is.
(defn open-file-limit-check
  []
  (loop [files []]
    (let [x (try
              (RandomAccessFile. (str "tmp" (count files)) "rw")
              (catch IOException e
                e))]
      (if (instance? IOException x)
        (do
          (println "IOException after " (count files) " open files:" (str x))
          (doseq [file files
                  n (range 0 (count files))]
            (.close file)
            (.delete (File. (str "tmp" n)))))
        (recur (conj files x))))))

;;; http://lucene.apache.org/core/4_3_1/core/index.html?org/apache/lucene/util/RamUsageEstimator.html
;;; http://lucene.apache.org/core/3_6_2/api/core/index.html?org/apache/lucene/util/RamUsageEstimator.html
(defn size-of
  [obj]
  (RamUsageEstimator/sizeOf obj))

(defn human-size-of
  [obj]
  (RamUsageEstimator/humanSizeOf obj))

;;; MemoryMeter
(defn measure
  [obj]
  (.measure (MemoryMeter.) obj))

(defn measure-deep
  [obj]
  (.measureDeep (MemoryMeter.) obj))

(defn count-children
  [obj]
  (.countChildren (MemoryMeter.) obj))

;;; http://docs.oracle.com/javase/6/docs/api/index.html?java/lang/Runtime.html
(defn runtime
  []
  (Runtime/getRuntime))

(defn available-processors
  []
  (.availableProcessors (runtime)))

(defn free-memory
  []
  (.freeMemory (runtime)))

(defn max-memory
  []
  (.maxMemory (runtime)))

(defn exit
  ([]
     (exit 0))
  ([status-code]
     (.exit (runtime) status-code)))

;;; http://docs.oracle.com/javase/6/docs/api/index.html?java/lang/System.html
(defn get-env
  []
  (System/getenv))

(defn get-properties
  []
  (System/getProperties))

(defn nano-time
  []
  (System/nanoTime))

