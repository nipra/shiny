(ns shiny.utils.date-time
  (:import (org.joda.time Period Seconds DateTime DateTimeZone))
  (:import (org.joda.time.format PeriodFormatterBuilder PeriodFormatter))
  (:import (java.util TimeZone))
  (:require (clj-time [core :as ctc]
                      [format :as ctf]
                      [coerce :as ctco]
                      [local :as ctl])))

;;; http://joda-time.sourceforge.net/api-release/index.html?org/joda/time/format/ISODateTimeFormat.html
(def yyyyMMdd (ctf/formatter "yyyyMMdd"))

;;; http://en.wikipedia.org/wiki/List_of_tz_database_time_zones
;;; http://www.iana.org/time-zones
;;; http://joda-time.sourceforge.net/api-release/index.html?org/joda/time/DateTimeZone.html

(defn get-tz-ids
  []
  (vec (sort (DateTimeZone/getAvailableIDs))))

(defn get-tz-display-names
  []
  (sort (distinct (map (fn [x] (.getDisplayName (TimeZone/getTimeZone x)))
                       (TimeZone/getAvailableIDs)))))

(defn get-tz-display-names*
  []
  (reduce (fn [result x]
            (let [display-name (.getDisplayName (TimeZone/getTimeZone x))]
              (update-in result [display-name] conj x)))
          {}
          (TimeZone/getAvailableIDs)))

(defn get-tz-display-names+
  []
  (reduce (fn [result x]
            (let [display-name (.getDisplayName (.toTimeZone (ctc/time-zone-for-id x)))]
              (update-in result [display-name] conj x)))
          {}
          (get-tz-ids)))

;;; FIXME: Incomplete
(defn readable-secs
  [secs]
  (let [period (.toPeriod (Seconds/seconds secs))
        period-formatter (.toFormatter (doto (PeriodFormatterBuilder.)
                                         (.appendDays)
                                         (.appendSuffix " day" "days")))]
    (with-out-str (println (.print period-formatter period)))))

(defn yyyymmdd-range*
  [start stop]
  (let [formatter (ctf/formatter "yyyyMMdd")
        start-date (ctf/parse formatter start)
        stop-date (ctf/parse formatter stop)]
    (loop [dates [start]
           prev-date start-date
           next-date (ctc/plus prev-date (ctc/days 1))]
      (if (ctc/within? (ctc/interval start-date stop-date) next-date)
        (recur (conj dates (ctf/unparse formatter next-date))
               next-date
               (ctc/plus next-date (ctc/days 1)))
        (conj dates stop)))))

(defn yyyymmdd-range
  [start stop]
  (let [formatter (ctf/formatter "yyyyMMdd")
        start-date (ctf/parse formatter start)
        stop-date (ctf/parse formatter stop)
        date-range (iterate #(ctc/plus % (ctc/days 1)) start-date)
        within? (partial ctc/within? (ctc/interval start-date stop-date))
        dates (take-while within? date-range)
        format-date (partial ctf/unparse formatter)]
    (conj (vec (map format-date dates)) stop)))

(defn yyyymmdd->millis
  "`date' in yyyyMMdd format."
  [date]
  (ctco/to-long (ctf/parse yyyyMMdd date)))

(defn days->secs
  [days]
  (.getSeconds (.toStandardSeconds (ctc/days days))))

;;; Time between 2 datetimes
(defn range->ms
  [start end]
  (ctc/in-msecs (ctc/interval start end)))

(defn range->secs
  [start end]
  (ctc/in-secs (ctc/interval start end)))

(defn range->mins
  [start end]
  (ctc/in-minutes (ctc/interval start end)))

(defn range->hours
  [start end]
  (ctc/in-hours (ctc/interval start end)))

(defn range->days
  [start end]
  (ctc/in-days (ctc/interval start end)))

(defn range->weeks
  [start end]
  (ctc/in-days (ctc/interval start end)))

(defn range->months
  [start end]
  (ctc/in-months (ctc/interval start end)))

(defn range->years
  [start end]
  (ctc/in-years (ctc/interval start end)))

;;;
(defn within?
  [start end date]
  (ctc/within? (ctc/interval start end) date))

(comment
  ;; Examples
  (ctc/days 1)
  (ctc/plus (ctc/now) (ctc/days 1))
  (ctco/to-long (ctf/parse (:basic-date-time-no-ms ctf/formatters) "20120101T000000Z"))
  (ctco/to-date (ctco/to-long (ctf/parse (:basic-date ctf/formatters) "20120301")))
  (ctco/to-date-time (ctco/to-long (ctf/parse (:basic-date ctf/formatters) "20120301")))
  (ctco/to-long (ctl/to-local-date-time (ctf/parse utils.date-time/yyyyMMdd "20120301")))
  (ctf/unparse (:basic-date ctf/formatters)
               (ctf/parse (:basic-date ctf/formatters) "20120301"))
  (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-04-17T05:30Z"))

(comment
  (range->secs (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-04-17T05:30Z")
               (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-04-17T06:30Z"))
  (within? (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-04-17T05:30Z")
           (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-07-18T06:30Z")
           (ctf/parse (ctf/formatter "yyyy-MM-dd'T'HH:mmZZ") "2013-07-18T05:30Z")))
