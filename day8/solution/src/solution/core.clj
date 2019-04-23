(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn execute-test [op & args] 
  (cond 
    (= op "==") (apply = args)
    (= op "!=") (apply not= args)
    :else (apply (resolve (symbol op)) args)))

(defn condition? [[reg op num] env] 
  (let [reg-value (get env reg) 
        num-value (Integer/parseInt num)]
    (execute-test op reg-value num-value)))

(defn my-eval [[reg op num _ & test] env] 
  (if (condition? test env) 
    (case op 
      "inc" (update env reg + (Integer/parseInt num))
      "dec" (update env reg - (Integer/parseInt num)))
    env))

(defn parse-input [path] 
  (->> 
    (slurp path)
    (str/split-lines)
    (map #(str/split % #"\s+"))))

(defn process-input [input registers] 
  (loop [[x & xs :as lines] 
         input regs registers
         max-value 0] 
    (if (empty? lines) 
      [regs max-value]
      (let [new-regs (my-eval x regs) 
            max-so-far (apply max max-value (map val regs))] 
        (recur xs new-regs max-so-far)))))
    

(defn -main
  [& args]
  (let [input (parse-input "resources/input.txt")
        registers (zipmap (map first input) (repeat 0))
        [final-regs max-val] (process-input input registers)] 
    
    (do 
    (println (apply max (map val final-regs)))
    (println max-val))))

    
