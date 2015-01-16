(ns tic-tac-toe.game
  (:require [tic-tac-toe.board :as board]))

(defn start [board-size]
  {:board (board/empty-board board-size), :player :x})

(defn valid-move? [{:keys [board]} cell]
  (nil? (board/cell board cell)))

(defn next-player [{:keys [player]}]
  (if (= :x player)
    :o
    :x))

(defn move [{:keys [board player] :as state} cell]
  (if (valid-move? state cell)
    (assoc state :board  (board/mark-cell board cell player)
                 :player (next-player state))
    state))
