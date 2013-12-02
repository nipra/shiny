(defproject shiny "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]

  ;; For lein < 1.7.0
  ;; https://github.com/technomancy/swank-clojure/tree/master/lein-swank
  ;; :dev-dependencies [[swank-clojure "1.4.0"]]

  ;; nrepl
  ;; :repl-options {:port 4001}

  ;; For lein >= 1.7.0
  ;; https://github.com/technomancy/swank-clojure/tree/master/lein-swank
  :plugins [[lein-swank "1.4.5"]]
  )
