(ns solution.graph)

(defn make-child-set [element] 
  (let [child (drop 3 element)] 
    (filter identity 
      (conj 
        (map 
          #(subs % 0 (- (count %) 1)) 
          (butlast child)) 
        (last child)))))

(defn weight-children [input] 
  (map 
    #(-> 
      {:weight (Integer/parseInt (re-find #"\d+" (second %)))
       :children (make-child-set %)}) 
    input))

(defn make-graph [input] 
  (let [parents (map first input)
        node-data (weight-children input)] 
    (zipmap parents node-data)))

(defn node-weight [graph id] 
  (get-in graph [id :weight]))

(defn node-children [graph id] 
  (get-in graph [id :children]))

(defn node-total [graph id] 
  (get-in graph [id :total]))
