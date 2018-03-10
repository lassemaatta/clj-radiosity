(ns radiosity.wavefront.test_lines
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [radiosity.wavefront.core :as wavefront]))

(stest/instrument)

(deftest line-parsing
  (testing "parsing vertices"
    (let [result   (wavefront/line->element "v 1 2 3")
          expected {:type :vertex
                    :x    1.0 :y 2.0 :z 3.0}]
      (is (= result expected))))

  (testing "parsing faces"
    (let [result   (wavefront/line->element "f 1/2/3 4/5/6 7/8/9")
          expected {:type :face
                    :a    {:v 1 :t 2 :n 3}
                    :b    {:v 4 :t 5 :n 6}
                    :c    {:v 7 :t 8 :n 9}}]
      (is (= result expected))))

  (testing "parsing partially defined faces"
    (let [result   (wavefront/line->element "f 1 4//6 7//9")
          expected {:type :face
                    :a    {:v 1 :t nil :n nil}
                    :b    {:v 4 :t nil :n 6}
                    :c    {:v 7 :t nil :n 9}}]
      (is (= result expected)))))
