(ns tic-tac-toe.ai
  (:require [tic-tac-toe.util.minimax :refer [minimax]]
            [tic-tac-toe.game :as game]))

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
