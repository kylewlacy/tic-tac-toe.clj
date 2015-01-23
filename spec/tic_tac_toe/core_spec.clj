(ns tic-tac-toe.core-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.drawing :as draw]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.core :as ttt]))

(describe "tic-tac-toe"
  (context "when a user is selecting a cell"
    (with nums->3x3-cells {7 [1 1], 8 [1 2], 9 [1 3]
                           4 [2 1], 5 [2 2], 6 [2 3]
                           1 [3 1], 2 [3 2], 3 [3 3]})
    (it "properly assigns a number to a cell"
      (doseq [[num cell] @nums->3x3-cells]
        (should= num (ttt/cell->number [3 3] cell))))
    (it "properly gets a cell from a number"
      (doseq [[num cell] @nums->3x3-cells]
        (should= cell (ttt/number->cell [3 3] num)))))
  (context "when drawing a game"
    (around [it]
      (binding [draw/*charset* draw/ascii-charset]
        (it)))
    (it "can add numbers to blank cells in a board"
      (let [board (-> (board/empty-board [3 3])
                      (board/mark-cell [1 1] :x)
                      (board/mark-cell [2 2] :o)
                      (board/mark-cell [3 3] :x))]
        (should= [[:x 8  9]
                  [4  :o 6]
                  [1  2  :x]]
                 (ttt/board-with-numbers board))))
    (it "draws a game by drawing a board with numbers"
      (let [state (-> (game/start [3 3])
                      (game/move [1 3])
                      (game/move [2 2])
                      (game/move [3 1]))]
        (should= (str "7|8|X" \newline
                      "-+-+-" \newline
                      "4|O|6" \newline
                      "-+-+-" \newline
                      "X|2|3")
                 (ttt/state-str state))))
    (it "draws a move with a cell number"
      (let [state* (game/start [3 3])
            state (game/move state* [1 1])]
        (should= "X > 1" (ttt/move-str state* [3 1])))))
  (context "when the game is over"
    (around [it]
      (binding [ttt/*end-strs* {:win "winner", :lose "loser", :draw "draw"}]
        (it)))
    (it "tells the player if they won"
      (let [x-wins (-> (game/start [3 3])
                       (game/move [1 1])
                       (game/move [2 1])
                       (game/move [2 2])
                       (game/move [3 1])
                       (game/move [3 3]))
            o-wins (-> (game/start [3 3])
                       (game/move [2 1])
                       (game/move [1 1])
                       (game/move [3 1])
                       (game/move [2 2])
                       (game/move [3 2])
                       (game/move [3 3]))
            draw   (-> (game/start [3 3])
                       (game/move [1 2])
                       (game/move [1 1])
                       (game/move [2 1])
                       (game/move [1 3])
                       (game/move [2 3])
                       (game/move [2 2])
                       (game/move [3 1])
                       (game/move [3 2])
                       (game/move [3 3]))]
        (should= "winner" (ttt/game-end-str x-wins :x))
        (should= "loser" (ttt/game-end-str x-wins :o))
        (should= "loser" (ttt/game-end-str o-wins :x))
        (should= "winner" (ttt/game-end-str o-wins :o))
        (should= "draw" (ttt/game-end-str draw :x))
        (should= "draw" (ttt/game-end-str draw :o))))))
