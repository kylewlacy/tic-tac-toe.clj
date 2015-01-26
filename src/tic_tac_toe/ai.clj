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

(defn rotate-state [state rotations]
  (update-in state [:board] board/rotate rotations))

(defn mirror-state-x [state]
  (update-in state [:board] board/mirror-x))

(defn mirror-state-y [state]
  (update-in state [:board] board/mirror-y))

(defn mirror-state-diag-top-left [state]
  (update-in state [:board] board/mirror-diag-top-left))

(defn mirror-state-diag-bottom-left [state]
  (update-in state [:board] board/mirror-diag-bottom-left))

(defn states-isomorphic? [a b]
  (or (= a b)
      (= a (rotate-state b 1))
      (= a (rotate-state b 2))
      (= a (rotate-state b 3))
      (= a (mirror-state-x b))
      (= a (mirror-state-y b))
      (= a (mirror-state-diag-top-left b))
      (= a (mirror-state-diag-bottom-left b))))

(defn moves-isomorphic? [state a b]
  (states-isomorphic? (game/move state a) (game/move state b)))

(defn filter-unique-moves [state moves]
  (if (zero? (count moves))
    moves
    (let [move        (first moves)
          other-moves (rest moves)
          iso-moves   (filter #(moves-isomorphic? state move %)
                              other-moves)
          unique?     (empty? iso-moves)
          rest-unique (filter-unique-moves state other-moves)]
      (if unique?
        (concat [move] rest-unique)
        rest-unique))))

(defn unique-moves
  "For a given state, return a subset of the state's valid moves,
   filtering moves that are logically unique, and removing moves that
   are isomorphic. For example:
     (unique-moves (game/start [3 3])) => [1 1] [1 2] [2 2] ;; (or equivalent)
   That is, for an empty 3x3 game, the only distinct moves are a corner, an
   edge, or the center cell. This is useful for trimming minimax branches
   that are logically equivalent"
  [state]
  (filter-unique-moves state (game/valid-moves state)))

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
     (pick-move state (depth-required-for state)))
  ([state max-depth]
     (let [player (:player state)
           valuer (game/valuer-for player)
           moves  (minimax state
                           game/move
                           unique-moves
                           game/end?
                           valuer
                           #(= player (:player %))
                           max-depth)]
       (or (first moves) [1 1]))))

(defn move
  ([state]
     (move state (depth-required-for state)))
  ([state max-depth]
     (game/move state (pick-move state max-depth))))
