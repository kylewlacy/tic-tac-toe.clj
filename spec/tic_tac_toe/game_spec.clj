(ns tic-tac-toe.game-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.board :as board]))

(describe "the game logic"
  (context "when starting the game"
    (it "starts with Xs"
      (should= :x (:player (game/start [3 3]))))
    (it "starts with an empty board"
      (should= (repeat 9 nil) (-> (game/start [3 3]) :board board/cells))))
  (context "when making moves"
    (with state (game/start [3 3]))
    (it "lets a player make a valid move"
      (should= [[:x nil nil] [nil nil nil] [nil nil nil]]
               (-> @state
                   (game/move [1 1])
                   :board
                   board/rows))
      (should= [[:x nil nil] [nil nil :o] [nil nil nil]]
               (-> @state
                   (game/move [1 1])
                   (game/move [2 3])
                   :board
                   board/rows)))
    (it "does not let a player make an invalid move"
      (let [state* (game/move @state [1 2])]
        (should-be false? (game/valid-move? state* [1 2]))
        (should= state* (game/move state* [1 2]))))
    (it "alternates players"
      (should= :o (-> @state
                      (game/move [1 1])
                      :player))
      (should= :x (-> @state
                      (game/move [1 1])
                      (game/move [2 2])
                      :player)))
    (it "gets a seq of possible moves"
      (let [state* (game/move @state [1 2])]
        (should= [[1 1] [1 2] [1 3] [2 1] [2 2] [2 3] [3 1] [3 2] [3 3]]
                 (game/valid-moves @state))
        (should= [[1 1] [1 3] [2 1] [2 2] [2 3] [3 1] [3 2] [3 3]]
                 (game/valid-moves state*)))))
  (context "when determining a winner"
    (with state (game/start [3 3]))
    (with state-x-row (-> @state
                          (game/move [1 1]) (game/move [2 1])
                          (game/move [1 2]) (game/move [2 2])
                          (game/move [1 3])))
    (with state-x-col (-> @state
                          (game/move [1 1]) (game/move [1 2])
                          (game/move [2 1]) (game/move [2 2])
                          (game/move [3 1])))
    (with state-x-diag (-> @state
                           (game/move [1 1]) (game/move [1 3])
                           (game/move [2 2]) (game/move [2 3])
                           (game/move [3 3])))
    (with state-o-row (-> @state
                          (game/move [1 1]) (game/move [2 1])
                          (game/move [1 2]) (game/move [2 2])
                          (game/move [3 3]) (game/move [2 3])))
    (with state-draw (-> @state
                         (game/move [3 1]) (game/move [3 2])
                         (game/move [3 3]) (game/move [1 1])
                         (game/move [1 2]) (game/move [1 3])
                         (game/move [2 1]) (game/move [2 2])
                         (game/move [2 3])))
    (it "determines when the game has been won"
      (should-be false? (game/win? @state))
      (should-be false? (game/win? @state-draw))
      (should-be true? (game/win? @state-x-row))
      (should-be true? (game/win? @state-x-col))
      (should-be true? (game/win? @state-x-diag))
      (should-be true? (game/win? @state-o-row)))
    (it "determines the winner"
      (should= nil (game/maybe-winner @state))
      (should= nil (game/maybe-winner @state-draw))
      (should= :x (game/maybe-winner @state-x-row))
      (should= :x (game/maybe-winner @state-x-col))
      (should= :x (game/maybe-winner @state-x-diag))
      (should= :o (game/maybe-winner @state-o-row))
      (should-be false? (game/winner? @state :x))
      (should-be false? (game/winner? @state :o))
      (should-be true? (game/winner? @state-x-row :x))
      (should-be false? (game/winner? @state-x-row :o))
      (should-be false? (game/winner? @state-o-row :x))
      (should-be true? (game/winner? @state-o-row :o)))
    (it "determines if the game is a draw"
      (should-be false? (game/draw? @state))
      (should-be true? (game/draw? @state-draw))
      (should-be false? (game/draw? @state-x-row))
      (should-be false? (game/draw? @state-x-col))
      (should-be false? (game/draw? @state-x-diag))
      (should-be false? (game/draw? @state-o-row)))
    (it "determines if the game is over"
      (should-be false? (game/end? @state))
      (should-be true? (game/end? @state-draw))
      (should-be true? (game/end? @state-x-row))
      (should-be true? (game/end? @state-x-col))
      (should-be true? (game/end? @state-x-diag))
      (should-be true? (game/end? @state-o-row)))))
