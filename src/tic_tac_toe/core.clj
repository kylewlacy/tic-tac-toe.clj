(ns tic-tac-toe.core
  (:require [clojure.string :as str]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.drawing :as draw]
            [tic-tac-toe.ai :as ai]))

(def ^:dynamic *end-strs* {:win "You won!"
                           :lose "You lost!"
                           :draw "It's a draw"})

(defn cell->number
  "Given a board size and a board cell location, return a single number used to
   index a cell location. For a 3x3 board, the result of `cell->number` will
   map a tic-tac-toe cell to the numpad key number in the same position"
  [[rows cols] [row col]]
  (+ col (* cols (- rows row))))

(defn number->cell
  "Given a board size and a cell index number, return a cell location that
   the index number represents. For a 3x3 board, the result of `number->cell`
   will map a numpad key number to a tic-tac-toe cell in the same position"
  [[rows cols] num]
  (let [col (inc (mod (dec num) rows))
        row (- rows (quot (dec num) rows))]
    [row col]))

(defn maybe-mark-cell-number [board cell]
  (if (board/cell board cell)
    board
    (board/mark-cell board cell (cell->number (board/size board) cell))))

(defn board-with-numbers [board]
  (reduce maybe-mark-cell-number board (board/cell-locations board)))



(defn prompt [prompt-str]
  (print prompt-str "> ")
  (flush)
  (read-line))

(defn parse-prompt [parse-f prompt-str]
  (loop [input nil]
    (or input (recur (parse-f (prompt prompt-str))))))



(defn maybe-parse-int [int-str]
  (try
    (Integer/parseInt int-str)
    (catch NumberFormatException e
      nil)))

(defn parse-player-move-cell [state num-str]
  (let [num  (or (maybe-parse-int num-str) -1)
        cell (number->cell (board/size (:board state)) num)]
    (when (game/valid-move? state cell)
      cell)))

(defn parse-player-token [maybe-str]
  (let [maybe-kw (keyword (str/lower-case (or (first maybe-str) "")))]
    (when (or (= maybe-kw :x) (= maybe-kw :o))
      maybe-kw)))



(defn print-move [{:keys [player board] :as state} cell]
  (let [player-name (str/upper-case (name player))
        move-num    (int (cell->number (board/size board) cell))]
    (println \newline player-name ">" move-num))
  (game/move state cell))

(defn state-str [{:keys [board]}]
  (-> board
      board-with-numbers
      draw/board-str))

(defn print-state [state]
  (println (state-str state)))

(defn game-end-str [state player]
  (cond
    (game/winner? state player)
      (:win *end-strs*)
    (game/win? state)
      (:lose *end-strs*)
    (game/draw? state)
      (:draw *end-strs*)))

(defn print-game-end [state player]
  (println (game-end-str state player)))



(defn player-move [state]
  (let [parse-move  (partial parse-player-move-cell state)
        player-cell (parse-prompt parse-move "Enter a cell number")]
    (print-move state player-cell)))

(defn ai-move [state]
  (print-move state (ai/pick-move state)))

(defn do-move [state player]
  (if (= player (:player state))
    (player-move state)
    (ai-move state)))



(defn play-game [init-state player]
  (loop [state init-state]
    (print-state state)
    (if (game/end? state)
      (print-game-end state player)
      (recur (do-move state player)))))

(defn -main [& args]
  (let [player-prompt-str "Do you want to play as X's or O's?"
        player            (parse-prompt parse-player-token player-prompt-str)]
    (play-game (game/start [3 3]) player)))
