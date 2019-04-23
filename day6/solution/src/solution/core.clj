(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn redistribute [banks] 
  (let [max-bank (apply max banks) 
        max-index (.indexOf banks max-bank)
        len (count banks)] 
    (reduce 
      #(update %1 (rem %2 len) inc)
      (assoc banks max-index 0)
      (take max-bank (iterate inc (inc max-index))))))

(defn get-bank [path] 
  (->> 
    (str/split (slurp path) #"\s+")
    (map #(Integer. %))
    (into [])))

(defn solve[numbers] 
  (loop [counter 0 bank numbers checked {}] 
    (if (contains? checked bank) 
      [counter (- (count checked) (get checked bank))]
      (recur (inc counter) 
             (redistribute bank) 
             (conj checked {bank counter})))))

(defn -main
  [& args]
  (time (let [numbers (get-bank "resources/input.txt")] 
    (println (solve numbers)))))
