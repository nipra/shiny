(def HOME (System/getProperty "user.home"))
(def GC-LOG-OPTION (str "-Xloggc:" HOME "/jvm-logs/gc-shiny.log"))

(defproject shiny "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-time "0.4.2"]

                 ;; Java Agent for Memory Measurements
                 ;; https://github.com/jbellis/jamm
                 [com.github.stephenc/jamm "0.2.5"]

                 ;; Memory consumption estimator for Java
                 ;; https://github.com/dweiss/java-sizeof
                 [com.carrotsearch/java-sizeof "0.0.3"]]

  ;; For lein < 1.7.0
  ;; https://github.com/technomancy/swank-clojure/tree/master/lein-swank
  ;; :dev-dependencies [[swank-clojure "1.4.0"]]

  ;; nrepl
  ;; :repl-options {:port 4001}

  ;; For lein >= 1.7.0
  ;; https://github.com/technomancy/swank-clojure/tree/master/lein-swank
  :plugins [[lein-swank "1.4.5"]]
  ;; -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:$HBASE_HOME/logs/gc-hbase.log
  ;; :jvm-opts ["-verbose:gc" "-XX:+PrintGCDetails" "-XX:+PrintGCDateStamps" "-Xloggc:/Users/nprabhak/jvm-logs/gc-shiny.log"]
  :jvm-opts ["-XX:+UseConcMarkSweepGC" "-XX:+CMSIncrementalMode"
             "-verbose:gc" "-XX:+PrintGCDetails"
             "-XX:+PrintGCDateStamps" ~GC-LOG-OPTION])
