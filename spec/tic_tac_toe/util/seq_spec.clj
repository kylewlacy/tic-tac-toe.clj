(ns tic-tac-toe.util.seq-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.util.seq :as seq]))

(context "util.seq"
  (describe "fetch"
    (it "gets a value by index in a seq"
      (should= :d (seq/fetch [:a :s :d :f] 2))
      (should= 50 (seq/fetch (map + (range) (range)) 25))
      (should= 4 (seq/fetch (list 4 5 6 7) 0)))
    (it "gets a value by key in a map"
      (should= 2 (seq/fetch {:a 1, :b 2, :c 3} :b)))
    (it "returns nil for invalid keys"
      (should= nil (seq/fetch {:a 1, :b 2} :c))
      (should= nil (seq/fetch '(a s d f) 4))
      (should= nil (seq/fetch [1 2 3 4 5] -1))
      (should= nil (seq/fetch [:foo :bar] :baz))))
  (describe "fetch-in"
    (it "gets a value from a nested seq"
      (should= 3
               (seq/fetch-in [{:a 1, :b [:c {:d [2 3]}]}] [0 :b 1 :d 1]))
      (should= [{:c 3, :d 4}]
               (seq/fetch-in {:a [1 2 {:b [{:c 3, :d 4}]}]} [:a 2 :b]))))
  (describe "with"
    (it "replaces a value by index in a seq"
      (should= [:foo :qux :baz]
               (seq/with [:foo :bar :baz] 1 :qux))
      (should= [6 10 11 :asdf]
               (seq/with (map inc [5 9 10 11]) 3 :asdf))
      (should= '(j s d f)
               (seq/with '(a s d f) 0 'j)))
    (it "associates a key with a value in a map"
      (should= {:a 1, :b 2, :c 3, :d 4}
               (seq/with {:a 1, :b 2, :c 3} :d 4))
      (should= {:a 1, :b 2}
               (seq/with {:a 1, :b 3} :b 2))))
  (describe "with-in"
    (it "replaces a value in a nested seq"
      (should= [{:foo 1, :bar 3} {:baz 4, :qux 5} 6]
               (seq/with-in [{:foo 2, :bar 3} {:baz 4, :qux 5} 6] [0 :foo] 1))
      (should= {:a [:a1 [:a2 :a3]], :b [:b1 :b2]}
               (seq/with-in {:a [:a1 [:a2 :a4]], :b [:b1 :b2]} [:a 1 1] :a3)))))
