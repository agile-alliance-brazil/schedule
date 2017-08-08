(ns schedule.sample-test
  (:use clojure.test schedule.test-helper)
  (:require [schedule.web :as web]))

(deftest dev-mode-test
  (testing "should be true"
    (let [result (web/in-dev?)]
      (is (= true result)))))
