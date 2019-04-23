(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn parse-input [path] 
  (->> 
    (slurp path)
    (str/split-lines)
    (map #(Integer/parseInt (re-find #"\d+" %)))))

(defn transform [n factor] 
  (let [prod (* n factor)
        g (+ (bit-and prod 0x7FFFFFFF) (bit-shift-right prod 31))]
    (if (>= g 0x7FFFFFFF)
      (- g 0x7FFFFFFF)
      g)))

(defn generate [n factor] 
  (lazy-seq (cons n (generate (transform n factor) factor))))

(defn generate2 [n factor divisor] 
  (if (= (rem n divisor) 0) 
    (cons n (lazy-seq (generate2 (transform n factor) factor divisor))) 
    (generate2 (transform n factor) factor divisor)))

(defn test [a b] 
  (= (bit-and a 0xFFFF) (bit-and b 0xFFFF)))

(defn solve [path] 
  (let [[a b] (parse-input path)
        generateA (generate2 (transform a 16807) 16807 4)
        generateB (generate2 (transform b 48271) 48271 8)]
    (count
      (filter 
        #(apply test %)
        (take 5000000 (map vector generateA generateB))))))

(defn -main
  [& args]
  (time (println (solve "resources/input.txt"))))
