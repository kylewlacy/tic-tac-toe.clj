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

(defn make-transform
  "Return a function that transforms a board. Two functions are reqired to
   describe a transformation:
     size-f          - A function that takes the size of the current board,
                       and returns the size of the transformed board.
     transform-loc-f - A function that takes the old board size and a cell
                       location from the old board, and returns the cell
                       location that the old cell occupies in the new board.
                       For example, for a function that rotates a board once
                       clockwise, `transform-cell-f` should take the following
                       inputs, and produce the following outputs:
                         (transform-loc-f [3 3] [1 1]) => [1 3]
                         (transform-loc-f [3 3] [1 2]) => [2 3]
                         (transform-loc-f [3 3] [1 3]) => [3 3]
                         (transform-loc-f [3 3] [2 1]) => [1 2]
                         ...
                         (transform-loc-f [3 3] [3 3]) => [3 1]"
  [size-f transform-loc-f]
  (fn [board]
    (let [board-size  (size board)
          board*-size (size-f board-size)]
      (reduce (fn [board* cell-loc]
                (let [marker          (cell board cell-loc)
                      transformed-loc (transform-loc-f board-size cell-loc)]
                  (mark-cell board* transformed-loc marker)))
              (empty-board board*-size)
              (cell-locations board)))))

(def rotate1
  (make-transform (fn [[rows cols]]
                    [cols rows])
                  (fn [[rows cols] [row col]]
                    [col (inc (- rows row))])))

(defn rotate [board rotations]
  (if (zero? (mod rotations 4))
    board
    (rotate (rotate1 board) (dec rotations))))

(def mirror-x
  (make-transform identity
                  (fn [[rows cols] [row col]]
                    [row (inc (- cols col))])))

(def mirror-y
  (make-transform identity
                  (fn [[rows cols] [row col]]
                    [(inc (- rows row)) col])))

(def mirror-diag-top-left
  (make-transform identity
                  (fn [[rows cols] [row col]]
                    [col row])))

(def mirror-diag-bottom-left
  (make-transform identity
                  (fn [[rows cols] [row col]]
                    [(inc (- cols col)) (inc (- rows row))])))
