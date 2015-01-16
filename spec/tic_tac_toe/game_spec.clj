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
                      :player)))))
