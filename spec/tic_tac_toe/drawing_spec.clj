(ns tic-tac-toe.drawing-spec
  (:require [speclj.core :refer :all]
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
      (should= 9 (count (draw/center-str "foo" 9))))))
