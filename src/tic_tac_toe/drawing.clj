(ns tic-tac-toe.drawing)

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
