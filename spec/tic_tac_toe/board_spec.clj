(ns tic-tac-toe.board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :as board]))

(describe "a board"
  (context "when creating a board"
    (it "can be created with arbitrary dimensions"
      (should= [[nil nil nil]
                [nil nil nil]
                [nil nil nil]]
               (board/empty-board [3 3]))
      (should= [[nil nil nil nil]
                [nil nil nil nil]]
               (board/empty-board [4 2]))
      (should= [] (board/empty-board [0 0])))
    (it "can get its dimensions"
      (should= [3 3] (board/size (board/empty-board [3 3])))
      (should= [4 2] (board/size (board/empty-board [4 2])))
      (should= [0 0] (board/size (board/empty-board [0 0])))))
  (context "when getting and setting cells"
    (with board (board/empty-board [3 3]))
    (it "can mark cells"
      (should= [[nil nil nil]
                [nil :x  nil]
                [nil nil nil]]
               (board/mark-cell @board [2 2] :x))
      (should= [[nil :x  nil]
                [nil :o  nil]
                [nil nil nil]]
               (-> @board
                   (board/mark-cell [1 2] :x)
                   (board/mark-cell [2 2] :o))))
    (it "can get a marker in a cell"
      (should= :x (-> @board
                      (board/mark-cell [1 3] :x)
                      (board/cell [1 3])))
      (should= :o (-> @board
                      (board/mark-cell [3 3] :x)
                      (board/mark-cell [2 2] :o)
                      (board/cell [2 2])))))
  (context "when retrieving rows, columns, and diagonals"
    (with board (-> (board/empty-board [3 3])
                    (board/mark-cell [1 1] :x)
                    (board/mark-cell [1 3] :o)
                    (board/mark-cell [2 2] :x)
                    (board/mark-cell [2 1] :o)
                    (board/mark-cell [3 2] :x)
                    (board/mark-cell [2 3] :o)))
    (it "can get a seq of rows"
      (should= [[:x  nil :o]
                [:o  :x  :o]
                [nil :x  nil]]
               (board/rows @board)))
    (it "can get a seq of columns"
      (should= [[:x  :o nil]
                [nil :x :x]
                [:o  :o nil]]
               (board/cols @board)))
    (it "can get a seq of diagonals"
      (should= [[:x :x nil]
                [:o :x nil]]
               (board/diags @board)))))
