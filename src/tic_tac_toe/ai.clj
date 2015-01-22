(ns tic-tac-toe.ai
  (:require [tic-tac-toe.board :as board]
            [tic-tac-toe.game :as game]))

(def unlimited-depth -1)

(defn minimax
  ([init-state move-f moves-f terminal?-f value-f max?-f]
     (minimax init-state move-f moves-f terminal?-f value-f max?-f unlimited-depth))
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

(defn depth-required-for
  "Returns the minimum minimax depth required to play a perfect game for a
   given state (see https://gist.github.com/kylewlacy/867c67b15c298d5204c4
   for an example of how to compute this)"
  [state]
  ;; TODO: Find a formula to compute the depth for any given board size
  (condp = (board/size (:board state))
    [3 3] 6
    unlimited-depth))

(defn pick-move
  ([state]
     (pick-move state unlimited-depth))
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
