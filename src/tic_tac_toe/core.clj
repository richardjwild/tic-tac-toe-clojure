(ns tic-tac-toe.core
  (:require [clojure.set :as set]))

(def new-game {:state :game-on :to-play :x :board {}})

(def winning-combos (list #{:top-left :top-middle :top-right}
                          #{:centre-left :centre-middle :centre-right}
                          #{:bottom-left :bottom-middle :bottom-right}
                          #{:top-left :centre-left :bottom-left}
                          #{:top-middle :centre-middle :bottom-middle}
                          #{:top-right :centre-right :bottom-right}
                          #{:top-left :centre-middle :bottom-right}
                          #{:bottom-left :centre-middle :top-right}))

(defn- advance [player]
  (if (= player :x) :o :x))

(defn- full? [board]
  (= 9 (count board)))

(defn- taken-by? [who [_ player]]
  (= who player))

(defn- squares-taken-by [who board]
  (->> board
      (filter (partial taken-by? who))
      (map first)
      (set)))

(defn- all-taken? [taken-squares combo]
  (set/subset? combo taken-squares))

(defn- won? [board who]
  (let [their-squares (squares-taken-by who board)
        are-they-all-taken? (partial all-taken? their-squares)]
    (some are-they-all-taken? winning-combos)))

(defn- game-state [board]
  (cond (full? board) :stalemate
        (won? board :x) :x-has-won
        (won? board :o) :o-has-won
        :else :game-on))

(defn- game-over? [state]
  (contains? #{:stalemate :x-has-won :o-has-won} state))

(defn- game
  ([new-board new-state]
   (hash-map :board new-board :state new-state))
  ([new-board new-state to-play]
   (hash-map :board new-board :state new-state :to-play to-play)))

(defn- take-square [player square board]
  (let [new-board (assoc board square player)
        state (game-state new-board)]
    (if (game-over? state)
      (game new-board state)
      (game new-board state (advance player)))))

(defn- square-already-played [game]
  (assoc game :state :square-already-played))

(defn play [game square]
  (cond (game-over? (:state game)) game                     ; return the same game if it's over
        :else (let [{board :board, player :to-play} game]
                (if (contains? board square)
                  (square-already-played game)
                  (take-square player square board)))))