(ns solution.core
  (:gen-class))

(defn part1 [input] 
  (let [size (Math/ceil (Math/sqrt input))
        center (Math/ceil (/ (dec size) 2))] 
    (max 0 (+ (dec center) (Math/abs (- center (rem input size)))))))

(def neighbors [[1 0] [0 1] [-1 0] [0 -1] [1 1] [-1 -1] [-1 1] [1 -1]])

(def dirs {:n [0 1] :s [0 -1] :e [-1 0] :w [1 0]})

(defn get-coord [coord dir] 
  (map + coord (dir dirs)))

(defn next-coord-and-dir [[x y :as coord] dir] 
  (let [[x1 y1] (get-coord coord dir)] 
    (cond 
      (and (= dir :n) (= x1 y1)) [[x1 y1] :e]
      (and (= dir :e) (= (- x1) y1)) [[x1 y1] :s]
      (and (= dir :s) (= x1 y1)) [[x1 y1] :w]
      (and (= dir :w) (= x1 (inc (- y1)))) [[x1 y1] :n]
      :else [[x1 y1] dir])))

(defn value [s m] 
  (if (= s [0 0]) 
    1 
    (->> 
      (map #(map + %1 s) neighbors)
      (map #(get m % 0))
      (reduce +))))

(defn spiral [input] 
  (loop [s [0 0] dir :w m {}] 
    (let [x (value s m)] 
      (if (> x input)
        x
        (let [[x1 d] (next-coord-and-dir s dir)] 
          (recur x1 d (assoc m s x)))))))

(defn -main
  [& args]
  (println (spiral 277678)))
