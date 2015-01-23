(ns tic-tac-toe.board)

(defn empty-board [[rows cols]]
  (vec (repeat cols (vec (repeat rows nil)))))

(defn size [board]
  (let [cols (count board)
        rows (count (first board))]
    [rows cols]))

(defn mark-cell [board [row col] marker]
  (assoc-in board [(dec row) (dec col)] marker))

(defn cell [board [row col]]
  (get-in board [(dec row) (dec col)]))

(defn rows [board]
  board)

(defn cols [board]
  (apply map vector board))

(defn diags [board]
  (let [[rows cols] (size board)]
    (if (= rows cols)
      [(map #(get-in board [% %]) (range rows))
       (map #(get-in board [% (- rows % 1)]) (range rows))])))

(defn cells [board]
  (apply concat board))

(defn cell-locations [board]
  (let [[rows cols] (size board)]
    (for [row (range rows), col (range cols)]
      [(inc row) (inc col)])))
