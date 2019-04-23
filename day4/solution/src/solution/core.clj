(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn process-input [file-name] 
  (->> 
    (slurp file-name) 
    (str/split-lines)
    (map #(str/split % #"\s+"))))

(defn part1 [file-name] 
  (->> 
    (process-input file-name)
    (filter #(apply distinct? %))
    (count)))

(defn part2 [file-name] 
  (->> 
    (process-input file-name)
    (map #(map sort %))
    (filter #(apply distinct? %))
    (count)))

(defn -main
  [& args]
  (println (part1 "resources/input.txt")))
