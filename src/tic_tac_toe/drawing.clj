(ns tic-tac-toe.drawing
  (:require [clojure.string :as str]
            [tic-tac-toe.board :as board]))

(def ^:dynamic *row*        "━")
(def ^:dynamic *col*        "┃")
(def ^:dynamic *row+col*    "╋")
(def ^:dynamic *markers*    {:x "╳", :o "◯"})
(def ^:dynamic *cell-width* 3)

(defn pad-str [str* left-padding right-padding]
  (let [left-pad  (apply str (repeat left-padding " "))
        right-pad (apply str (repeat right-padding " "))]
    (str left-pad str* right-pad)))

(defn center-str [str* width]
  (let [width*        (count str*)
        padding       (max (- width width*) 0)
        left-padding  (quot padding 2)
        right-padding (- padding left-padding)]
    (pad-str str* left-padding right-padding)))

(defn cell-str* [board-cell]
  (or (get *markers* board-cell) ""))

(defn cell-str [board-cell]
  (center-str (cell-str* board-cell) *cell-width*))

(defn row-str [board-row]
  (str/join *col* (map cell-str board-row)))

(defn board-str [board]
  (let [[rows cols]    (board/size board)
        cell-separator (apply str (repeat *cell-width* *row*))
        row-separator  (str/join *row+col* (repeat cols cell-separator))]
    (str/join (str \newline row-separator \newline)
              (map row-str (board/rows board)))))
