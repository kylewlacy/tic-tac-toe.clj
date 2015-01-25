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
  (context "when getting seqs of cells"
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
               (board/diags @board)))
    (it "can get a seq of cells"
      (should= [:x  nil :o
                :o  :x  :o
                nil :x  nil]
               (board/cells @board)))
    (it "can get a seq of cell locations"
      (should= [[1 1] [1 2] [1 3]
                [2 1] [2 2] [2 3]
                [3 1] [3 2] [3 3]]
               (board/cell-locations (board/empty-board [3 3])))
      (should= [[1 1] [1 2] [1 3] [1 4]
                [2 1] [2 2] [2 3] [2 4]
                [3 1] [3 2] [3 3] [3 4]
                [4 1] [4 2] [4 3] [4 4]]
               (board/cell-locations (board/empty-board [4 4])))))
  (context "when being transformed"
    (with board [[:a :b :c]
                 [:d :e :f]
                 [:g :h :i]])
    (it "can be rotated"
      (should= [[:g :d :a]
                [:h :e :b]
                [:i :f :c]]
               (board/rotate @board 1))
      (should= [[:i :h :g]
                [:f :e :d]
                [:c :b :a]]
               (board/rotate @board 2))
      (should= [[:c :f :i]
                [:b :e :h]
                [:a :d :g]]
               (board/rotate @board 3))
      (should= @board (board/rotate @board 4))
      (should= @board (board/rotate @board 0))
      (should= (board/rotate @board 3) (board/rotate @board -1)))
    (it "can be mirrored"
      (should= [[:c :b :a]
                [:f :e :d]
                [:i :h :g]]
               (board/mirror-x @board))
      (should= [[:g :h :i]
                [:d :e :f]
                [:a :b :c]]
               (board/mirror-y @board))
      (should= [[:a :d :g]
                [:b :e :h]
                [:c :f :i]]
               (board/mirror-diag-top-left @board))
      (should= [[:i :f :c]
                [:h :e :b]
                [:g :d :a]]
               (board/mirror-diag-bottom-left @board)))))
