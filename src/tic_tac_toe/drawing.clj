(ns tic-tac-toe.drawing
  (:require [clojure.string :as str]
            [tic-tac-toe.board :as board]))

(def unicode-charset {:row        "━"
                      :col        "┃"
                      :row+col    "╋"
                      :markers    {:x "╳", :o "◯"}
                      :cell-width 3})

(def ascii-charset {:row        "-"
                    :col        "|"
                    :row+col    "+"
                    :markers    {:x "X", :o "O"}
                    :cell-width 1})

(def ^:dynamic *charset* unicode-charset)

(defn row [] (:row *charset*))
(defn col [] (:col *charset*))
(defn row+col [] (:row+col *charset*))
(defn markers [] (:markers *charset*))
(defn cell-width [] (:cell-width *charset*))

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
  (or (get (markers) board-cell) (str board-cell)))

(defn cell-str [board-cell]
  (center-str (cell-str* board-cell) (cell-width)))

(defn row-str [board-row]
  (str/join (col) (map cell-str board-row)))

(defn board-str [board]
  (let [[rows cols]    (board/size board)
        cell-separator (apply str (repeat (cell-width) (row)))
        row-separator  (str/join (row+col) (repeat cols cell-separator))]
    (str/join (str \newline row-separator \newline)
              (map row-str (board/rows board)))))
