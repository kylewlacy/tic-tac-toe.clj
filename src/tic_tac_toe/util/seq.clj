(ns tic-tac-toe.util.seq)

(defn fetch
  "Given an associative key or an index, get the item from `coll`
   associated with that key or index:
     (fetch [:a :b :c] 1) => :b
     (fetch {:a 1, :b 2} :a) => 1"
  [coll key-or-idx]
  (if (associative? coll)
    (get coll key-or-idx)
    (let [taken-count (inc key-or-idx)
          taken       (take taken-count coll)]
      (when (= taken-count (count taken))
        (last taken)))))

(defn fetch-in
  "Recursively fetch the items within `coll` associated with the
   keys or indieices within `keys`:
     (fetch-in {:a [1 2 3], :b [4 5 6]} [:b 1]) => 5"
  [coll keys]
  (cond
    (empty? keys)
      nil
    (= 1 (count keys))
      (fetch coll (first keys))
    :else
      (fetch-in (fetch coll (first keys)) (rest keys))))

(defn with
  "Return a copy of `coll` with the value of a given key/index set to `val`:
     (with [:a :b :c] 1) => :b"
  [coll key-or-idx val]
  (if (associative? coll)
    (assoc coll key-or-idx val)
    (concat (take key-or-idx coll)
            [val]
            (drop (inc key-or-idx) coll))))

(defn with-in
  "Return a copy of `coll`, with the item within the `keys` path
   set to `val`:
     (with-in {:a [[1 2 3] [4 5 6]]} [:a 0 1] :foo) =>
       {:a [[1 :foo 3] [4 5 6]]}"
  [coll [key & other-keys :as keys] val]
  (cond
    (empty? keys)
      coll
    (empty? other-keys)
      (with coll key val)
    :else
      (with coll key (with-in (fetch coll key) other-keys val))))
