(ns tic-tac-toe.board
  (:require [tic-tac-toe.util.seq :as seq]))

(defn empty-board [[rows cols]]
  (repeat cols (repeat rows nil)))

(defn size [board]
  (let [cols (count board)
        rows (count (first board))]
    [rows cols]))

(defn mark [board [row col] marker]
  (seq/with-in board [(dec row) (dec col)] marker))

(defn marker-at [board [row col]]
  (seq/fetch-in board [(dec row) (dec col)]))
