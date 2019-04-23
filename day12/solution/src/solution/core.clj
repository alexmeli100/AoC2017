(ns solution.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.tools.trace :as trace])
  (:gen-class))

(defn split-input [path] 
  (with-open [rdr (io/reader path)] 
    (doall (map #(str/split % #" <-> ") (line-seq rdr)))))

(defn get-ids [input] 
  (into [] (map #(Integer/parseInt (first %)) input)))

(defn get-connections [input] 
  (map #(str/split (second %) #", ") input))

(defn find-root [ids p] 
  (let [root (get ids p)] 
    (if (= root p)
      root
      (find-root ids root))))

(defn union [p, q, [ids sizes count]] 
  (let [i (find-root ids p)
        j (find-root ids q)] 
    (if (not= i j) 
      (if (< (get sizes i) (get sizes j)) 
        [(assoc ids i j) (update sizes j + (get sizes i)) (dec count)]
        [(assoc ids j i) (update sizes i + (get sizes j)) (dec count)])
      [ids sizes count])))

(defn solve [path] 
  (let [input (split-input path)
        ids (get-ids input)
        connections (get-connections input)
        sizes (into [] (take (count ids) (repeat 1)))
        length (count ids)] 
    (loop [val 0 [x & xs] connections y ids z sizes groups length] 
      (if (= val length) 
        [y z groups]
        (let [[new-ids new-sizes new-count] 
                (reduce #(union val %2 %1) 
                        [y z groups] 
                        (map #(Integer/parseInt %) x))]
          (recur (inc val) xs new-ids new-sizes new-count))))))

(defn -main
  [& args]
  (let [[ids sizes groups] (solve "resources/input.txt")] 
    (do 
      (println (get sizes (find-root ids 0)))
      (println groups))))
