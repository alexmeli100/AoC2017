(ns solution.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:gen-class))

(defn process [line] 
  (map #(Integer. %) (str/split line #": ")))

(defn parse-input [path] 
  (with-open [rdr (io/reader path)] 
    (doall (map process (line-seq rdr)))))

(defn caught?
  [[layer range] & {:keys [delay] :or {delay 0}}]
  (= (mod (+ delay layer) (* (- range 1) 2)) 0))

(defn is-caught? [delay input]
  (some #(caught? % :delay delay) input))

(defn part1 [input] 
  (->> 
    (filter #(caught? % :delay 0) input)
    (map #(* (first %) (second %)))
    (reduce +)))

(defn part2 [input] 
  (->> 
    (range)
    (some #(when (not (is-caught? % input)) %))))

(defn -main
  [& args]
  (let [input (parse-input "resources/input.txt")] 
    (time (println (part2 input)))))
