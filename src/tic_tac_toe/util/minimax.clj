(ns tic-tac-toe.util.minimax)

(defn minimax
  ([init-state move-f moves-f terminal?-f value-f max?-f]
     (minimax init-state move-f moves-f terminal?-f value-f max?-f -1))
  ([init-state move-f moves-f terminal?-f value-f max?-f max-depth]
     (letfn [(reduce-moves [moves] (reduce move-f init-state moves))
             (moves-value [moves] (value-f (reduce-moves moves)))
             (minimax* [state moves depth]
               (if (or (terminal?-f state) (zero? depth))
                 moves
                 (let [next-minimax #(minimax* (move-f state %)
                                               (concat moves [%])
                                               (dec depth))
                       max?         (max?-f state)
                       next-moves   (map next-minimax (moves-f state))
                       select-key   (if max? max-key min-key)]
                   (apply select-key moves-value next-moves))))]
       (minimax* init-state [] max-depth))))
