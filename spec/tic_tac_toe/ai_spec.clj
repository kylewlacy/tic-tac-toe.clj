(ns tic-tac-toe.ai-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game :as game]
            [tic-tac-toe.ai :as ai]))

(defn play-next-moves [state]
  (map #(game/move state %) (game/valid-moves state)))

(defn play-all-games [state]
  (if (game/end? state)
    [state]
    (mapcat (comp play-all-games ai/move) (play-next-moves state))))

(defn includes? [seq- item]
  (not (empty? (filter #(= % item) seq-))))

(describe "the tic-tac-toe AI"
  (context "when computing isomorphic states"
    (it "determines if two states are isomorphic"
      (let [state  (game/start [3 3])
            state2 (game/start [3 3])
            a1     (game/move state [1 1])
            a2     (game/move state [3 3])
            b      (game/move state [2 2])
            c      (game/move state [1 2])]
        (should-be true? (ai/states-isomorphic? state state))
        (should-be true? (ai/states-isomorphic? state state2))
        (should-be false? (ai/states-isomorphic? state a1))
        (should-be false? (ai/states-isomorphic? state a2))
        (should-be false? (ai/states-isomorphic? state b))
        (should-be false? (ai/states-isomorphic? state c))
        (should-be true? (ai/states-isomorphic? state2 state))
        (should-be true? (ai/states-isomorphic? state2 state2))
        (should-be false? (ai/states-isomorphic? state2 a1))
        (should-be false? (ai/states-isomorphic? state2 a2))
        (should-be false? (ai/states-isomorphic? state2 b))
        (should-be false? (ai/states-isomorphic? state2 c))
        (should-be false? (ai/states-isomorphic? a1 state))
        (should-be false? (ai/states-isomorphic? a1 state2))
        (should-be true? (ai/states-isomorphic? a1 a1))
        (should-be true? (ai/states-isomorphic? a1 a2))
        (should-be false? (ai/states-isomorphic? a1 b))
        (should-be false? (ai/states-isomorphic? a1 c))
        (should-be false? (ai/states-isomorphic? a2 state))
        (should-be false? (ai/states-isomorphic? a2 state2))
        (should-be true? (ai/states-isomorphic? a2 a1))
        (should-be true? (ai/states-isomorphic? a2 a2))
        (should-be false? (ai/states-isomorphic? a2 b))
        (should-be false? (ai/states-isomorphic? a2 c))
        (should-be false? (ai/states-isomorphic? b state))
        (should-be false? (ai/states-isomorphic? b state2))
        (should-be false? (ai/states-isomorphic? b a1))
        (should-be false? (ai/states-isomorphic? b a2))
        (should-be true? (ai/states-isomorphic? b b))
        (should-be false? (ai/states-isomorphic? b c))
        (should-be false? (ai/states-isomorphic? c state))
        (should-be false? (ai/states-isomorphic? c state2))
        (should-be false? (ai/states-isomorphic? c a1))
        (should-be false? (ai/states-isomorphic? c a2))
        (should-be false? (ai/states-isomorphic? c b))
        (should-be true? (ai/states-isomorphic? c c))))
    (it "gets a seq of unique moves for a state"
      (let [state            (game/start [3 3])
            unique           (ai/unique-moves state)
            contains-edge?   (or (includes? unique [1 2])
                                 (includes? unique [2 3])
                                 (includes? unique [3 2])
                                 (includes? unique [2 1]))
            contains-corner? (or (includes? unique [1 1])
                                 (includes? unique [1 3])
                                 (includes? unique [3 3])
                                 (includes? unique [3 1]))
            contains-center? (includes? unique [2 2])]
        (should-be true?
                   (and contains-edge? contains-corner? contains-center?)))))
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
                   (ai/pick-move 2))))
    (it "will block an opponent's winning move"
      (should= [1 3]
               (-> (game/start [3 3])
                   (game/move [3 1])
                   (game/move [3 3])
                   (game/move [2 2])
                   (ai/pick-move 2))))
    (it "never loses a game as X"
      (let [first-move (ai/move (game/start [3 3]))
            all-games  (play-all-games first-move)
            games-lost (filter #(game/winner? % :o) all-games)]
        (should-be empty? games-lost)))
    (it "never loses a game as O"
      (let [all-games  (play-all-games (game/start [3 3]))
            games-lost (filter #(game/winner? % :x) all-games)]
        (should-be empty? games-lost)))))


