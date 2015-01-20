(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.ai :as ai]))

(describe "the tic-tac-toe AI"
  (context "when playing the game"
    (it "makes valid moves"
      (let [state  (game/start [3 3])
            state* (game/move state [3 3])]
        (should-be (partial game/valid-move? state) (ai/pick-move state 2))
        (should-be (partial game/valid-move? state*) (ai/pick-move state* 2))))
    (it "will pick a winning move"
      (should= [3 3]
                 (-> (game/start [3 3])
                     (game/move [3 1])
                     (game/move [1 1])
                     (game/move [2 1])
                     (game/move [2 2])
                     (game/move [1 3])
                     (ai/pick-move 1))))
    (it "will block an opponent's winning move"
      (should= [1 3]
               (-> (game/start [3 3])
                   (game/move [3 1])
                   (game/move [3 3])
                   (game/move [2 2])
                   (ai/pick-move 2))))))
