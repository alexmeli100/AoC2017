(ns solution.core
  (:require [clojure.string :as str]
            [clojure.set :as set] 
            [solution.graph :refer :all] 
            [clojure.java.io :as io])
  (:gen-class))

(defn split-input [path] 
  (with-open [rdr (io/reader path)] 
    (doall (map #(str/split % #"\s+") (line-seq rdr)))))

(defn make-children-set [input] 
  (loop [[x &  xs :as lines] input children #{}] 
    (cond 
      (empty? lines)  children
      (< (count x) 4) (recur xs children)
      :else 
        (let [child-set (make-child-set x)] 
          (recur xs (apply conj children child-set))))))

(defn part1 [input] 
  (let [parents (set (map first input)) 
        children (make-children-set input)] 
    (first (set/difference parents children))))

(defn total-weight [graph node] 
  (let [children (node-children graph node)
        weight (node-weight graph node)] 
    (if (empty? children) 
      weight
      (apply + weight (map #(total-weight graph %) children)))))

(defn get-all-weights [graph] 
    (reduce 
      #(assoc-in %1 [%2 :total] (total-weight %1 %2)) graph (keys graph)))

(defn get-index [coll elem] 
  (loop [[x & xs] coll val 0] 
    (if (= x elem) 
      val
      (recur xs (inc val)))))

(defn find-imbalance [graph root] 
  (let [children (node-children graph root) 
        totals (map #(node-total graph %) children)
        max-total (apply max totals)] 
    (if (<= (count (set totals)) 1) 
      nil
      (let [unbalanced-node (nth children (get-index totals max-total)) 
            deeper-imbalance (find-imbalance graph unbalanced-node)] 
        (if deeper-imbalance 
          deeper-imbalance
          (- (node-weight graph unbalanced-node)
             (- max-total (apply min totals))))))))

(defn part2 [graph root] 
  (let [new-graph (get-all-weights graph)] 
    (find-imbalance new-graph root)))

     
(defn -main
  [& args]
  (let [input (split-input "resources/input.txt")
              root (part1 input)
              graph (make-graph input)] 
    (println (part2 graph root))))
