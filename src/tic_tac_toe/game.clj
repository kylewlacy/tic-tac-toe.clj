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

(defn valid-moves [{:keys [board] :as state}]
  (let [[n-rows n-cols] (board/size board)
        rows            (map inc (range 0 n-rows))
        cols            (map inc (range 0 n-cols))
        rows+cols       (for [row rows, col cols] [row col])]
    (filter (partial valid-move? state) rows+cols)))
