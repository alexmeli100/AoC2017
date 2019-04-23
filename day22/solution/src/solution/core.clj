(ns solution.core
  (:require [complex.core :as com] 
            [clojure.java.io :as io])
  (:gen-class))

(def l (com/complex 0 1))
(def r (com/complex 0 -1))
(def rv (com/complex -1 0))

(defn parse-file [path] 
  (with-open [rdr (io/reader path)] 
    (into-array (line-seq rdr))))

(defn get-coords [lines] 
  (for [j (range (count lines)) 
        i (range (count (first lines)))
        :when (= (get-in lines [j i]) \#)] 
    (com/complex i (- j))))

(defn get-infected [coords] 
  (reduce #(assoc %1 %2 :i) {} coords))

(defn get-state [path] 
  (->> 
    (parse-file path)
    get-coords
    get-infected))

(defn update-state [coords pos status dir c in] 
  (let [new-dir (if c (com/* dir c) dir)] 
    {:coords (assoc coords pos status) 
     :dir new-dir
     :pos (com/+ pos new-dir)
     :in in}))

(defn new-state [{:keys [coords pos dir in]}] 
  (let [curr (get coords pos :c)] 
    (case curr 
      :c (update-state coords pos :w dir l in)
      :i (update-state coords pos :f dir r in)
      :w (update-state coords pos :i dir nil (inc in))
      :f (update-state coords pos :c dir rv in))))

(defn solve [init-state n] 
  (->> 
    (iterate new-state init-state)
    (drop n)
    first 
    :in))

(defn -main
  [& args]
  (let [coords (get-state "resources/input.txt")
        dir (com/complex 0 1)
        pos (com/complex 12 -12)] 
    (println (solve {:coords coords :pos pos :dir dir :in 0} 10000000))))
