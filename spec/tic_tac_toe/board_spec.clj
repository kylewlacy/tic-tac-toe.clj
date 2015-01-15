(ns tic-tac-toe.board-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.board :as board]))

(describe "a board"
  (it "can be created with dimensions"
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
    (should= [0 0] (board/size (board/empty-board [0 0]))))
  (it "can mark cells"
    (should= [[nil nil nil]
              [nil :x  nil]
              [nil nil nil]]
             (board/mark (board/empty-board [3 3]) [2 2] :x))
    (should= [[nil :x  nil]
              [nil :o  nil]
              [nil nil nil]]
             (-> (board/empty-board [3 3])
                 (board/mark [1 2] :x)
                 (board/mark [2 2] :o))))
  (it "can get a marker in a cell"
    (should= :x (-> (board/empty-board [3 3])
                    (board/mark [1 3] :x)
                    (board/marker-at [1 3])))
    (should= :o (-> (board/empty-board [3 3])
                    (board/mark [3 3] :x)
                    (board/mark [2 2] :o)
                    (board/marker-at [2 2])))))
