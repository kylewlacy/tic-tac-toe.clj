(ns tic-tac-toe.drawing-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :as board]
            [tic-tac-toe.drawing :as draw]))

(context "when drawing a game of tic-tac-toe"
  (describe "pad-str"
    (it "adds padding to a string"
      (should= "foo    " (draw/pad-str "foo" 0 4))
      (should= "  bar" (draw/pad-str "bar" 2 0))
      (should= " quux " (draw/pad-str "quux" 1 1))))
  (describe "center-str"
    (it "properly pads a string to be centered"
      (should= "  foobar  " (draw/center-str "foobar" 10))
      (should= " asdf " (draw/center-str "asdf" 6)))
    (it "adds proper padding for odd numbers"
      (should= 10 (count (draw/center-str "foo" 10)))
      (should= 9 (count (draw/center-str "foobar" 9)))
      (should= 9 (count (draw/center-str "foo" 9)))))
  (describe "drawing a board"
    (with board (-> (board/empty-board [3 3])
                    (board/mark-cell [1 1] :x)
                    (board/mark-cell [2 2] :o)
                    (board/mark-cell [2 3] :x)
                    (board/mark-cell [3 1] :o)
                    (board/mark-cell [3 3] :x)))
    (around [it]            
      (binding [draw/*row*        "-"
                draw/*col*        "|"
                draw/*row+col*    "+"
                draw/*markers*    {:x "X", :o "O"}
                draw/*cell-width* 1]
        (it)))
    (it "draws a board's row"
      (should= "X| | " (draw/row-str (first (board/rows @board))))
      (should= " |O|X" (draw/row-str (second (board/rows @board))))
      (should= "O| |X" (draw/row-str (last (board/rows @board)))))
    (it "draws a full board"
      (should= (str "X| | " \newline
                    "-+-+-" \newline
                    " |O|X" \newline
                    "-+-+-" \newline
                    "O| |X")
               (draw/board-str @board)))))
