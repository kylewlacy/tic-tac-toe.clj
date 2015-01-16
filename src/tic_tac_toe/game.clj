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

(defn maybe-winner* [cells]
  (if (apply = cells)
    (first cells)))

(defn some-winner [cell-groups]
  (some identity (map maybe-winner* cell-groups)))

(defn maybe-winner [{:keys [board]}]
  (or (some-winner (board/rows board))
      (some-winner (board/cols board))
      (some-winner (board/diags board))))

(defn win? [state]
  (boolean (maybe-winner state)))

(defn winner? [state player]
  (= player (maybe-winner state)))

(defn draw? [state]
  (and (not (win? state))
       (empty? (valid-moves state))))

(defn end? [state]
  (or (win? state) (draw? state)))
