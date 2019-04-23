(ns solution.core
  (:require [clojure.string :as str]
            [clojure.core.async :as async])
  (:gen-class))

(declare eval-ins)

(defn get-reg [env reg] 
  (if (number? reg) 
    reg
    (get env reg)))

(defn apply-op [env reg x op] 
  (let [new-env (update env reg op (get-reg env x))] 
    (update new-env :pc inc)))

(defn add-ins [env reg x] 
  (apply-op env reg x +))

(defn mul-ins [env reg x] 
  (apply-op env reg x *))

(defn mod-ins [env reg x] 
  (apply-op env reg x mod))

(defn set-ins [env reg x] 
  (let [new-env (assoc env reg (get-reg env x))] 
    (update new-env :pc inc)))

(defn jgz-ins [env reg x] 
  (if (> (get-reg env reg) 0) 
    (update env :pc + (get-reg env x))
    (update env :pc inc)))

(defn snd-ins [env reg out-chan] 
  (let [new-env (reduce #(update %1 %2 inc) env [:pc :counter]) ] 
      (async/>!! out-chan reg)
      new-env))

(defn rcv-ins [env reg in-chan] 
  (let [new-env (update env :pc inc)
        [result channel] 
          (async/alts!! [in-chan (async/timeout 500)])]      
    (if result 
      (assoc new-env reg result) 
      (assoc new-env :timeout true))))

(defn application? [ins] 
  (seq? ins))

(defn apply-ins [[ins & rest] env] 
  (apply 
    (ns-resolve 'solution.core (symbol (str ins "-ins"))) 
    env rest))

(defn eval-ins [ins env in-chan out-chan] 
  (cond 
    (= (first ins) "snd") 
      (snd-ins env (get-reg env (second ins)) out-chan)
    (= (first ins) "rcv")
      (rcv-ins env (second ins) in-chan)
    (application? ins) 
      (apply-ins ins env)
    :else (println "Can't perform operation")))

(defn make-env [instructions id] 
  (let [vars (map second instructions)
        regs (set (filter (complement number?) vars))] 
    (conj 
      (zipmap regs (take (count regs) (repeat 0)))
      {:pc 0 "p" id :counter 0})))

(defn run [instructions id in out] 
  (loop [env (make-env instructions id) pc (:pc env)] 
    (if (or (< pc 0) (> pc (- (count instructions) 1))) 
      (:counter env)
      (let [new-env (eval-ins (get instructions pc) env in out)] 
        (if (:timeout new-env) 
          (:counter env)
          (recur new-env (:pc new-env)))))))

(defn solve [instructions] 
  (let [ch1 (async/chan 10000)
        ch2 (async/chan 10000)
        prg1 (async/go (run instructions 0 ch1 ch2))
        prg2 (async/go (run instructions 1 ch2 ch1))]
    (println (async/<!! prg2))))

(defn parse [line] 
  (->> 
    (str/split line #" ")
    (map 
      #(if (re-find #"^[-]?\d+$" %) (Integer. %) %))))

(defn get-instructions [path] 
  (with-open [rdr (clojure.java.io/reader path)] 
    (reduce #(conj %1 (parse %2)) [] (line-seq rdr))))

(defn -main
  [& args]
  (let [instructions (get-instructions "resources/input.txt")]
    (time (solve instructions))))
