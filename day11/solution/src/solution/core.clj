(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))
(defn split-input [path]
  (-> 
    (slurp path)
    (str/split #",")))

(defn get-direction [[x y z] direction] 
  (case direction 
    "n" [x (inc y) (dec z)]
    "ne" [(inc x) y (dec z)]
    "se" [(inc x) (dec y) z]
    "s" [x (dec y) (inc z)]
    "sw" [(dec x) y (inc z)]
    "nw" [(dec x) (inc y) z]))

(defn distance [[x y z]] 
  (/ (+ (Math/abs x) (Math/abs y) (Math/abs z)) 2))

(defn solve [input] 
  (let [coords (reductions #(get-direction %1 %2) [0 0 0] input)
        part1 (distance (last coords))
        part2 (apply max (map distance coords))] 
    [part1 part2]))



(defn -main
  [& args]
  (let [input (split-input "resources/input.txt")
        [part1 part2] (solve input)] 
      (do 
        (println (str "Part1: " part1))
        (println (str "Part2: " part2)))))
