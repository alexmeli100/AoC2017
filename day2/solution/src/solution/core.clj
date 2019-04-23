(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn parse-input [path] 
  (->> 
    (slurp path) 
    (str/split-lines)
    (map #(str/split % #"\s+"))))

(defn find-divisibles [line] 
  (for [x line 
        y line
        :let [n2 (rem x y)]
        :when (and (not= x y) (= n2 0))] 
    (/ x y)))

(defn part1 [parsed-input] 
  (reduce 
    +
    (map 
      #(- (apply max %)
          (apply min %))
      parsed-input)))

(defn part2 [parsed-input] 
  (reduce 
    +
    (map 
      #(first (find-divisibles %))
      parsed-input)))

(defn -main
  [& args]
  (let [parsed-input 
          (for [line (parse-input "resources/input.txt")] 
            (map #(Integer/parseInt %) line))] 
    (println (part2 parsed-input))))
