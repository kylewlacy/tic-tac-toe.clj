(ns tic-tac-toe.ai
  (:require [tic-tac-toe.game :as game]))

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

(defn pick-move
  ([state]
     (pick-move state -1))
  ([state max-depth]
     (let [player (:player state)
           valuer (game/valuer-for player)
           moves  (minimax state
                           game/move
                           game/valid-moves
                           game/end?
                           valuer
                           #(= player (:player %))
                           max-depth)]
       (or (first moves) [1 1]))))

(defn move [state max-depth]
  (game/move state (pick-move state max-depth)))
