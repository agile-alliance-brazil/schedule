(ns schedule.db.migration
  (:gen-class)
  (:require [ragtime.jdbc :as jdbc]
    [ragtime.repl :as repl]
    [schedule.db.config :as config]))

(def ragtime-config
  {
    :datastore  (jdbc/sql-database config/dbspec)
    :migrations (jdbc/load-resources "migrations")
  })

(defn migrate []
  (repl/migrate ragtime-config))

(defn rollback []
  (repl/rollback ragtime-config))

(defn -main [& operations]
  (if (empty? operations)
    (migrate)
    (doall
      (map
        (fn [operation]
          (case operation
            "rollback" (rollback)
            (migrate)))
        operations))))
