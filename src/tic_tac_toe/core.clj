(ns tic-tac-toe.core
  (:require [clojure.set :as set]))

(def new-game {:state :game-on, :to-play :x, :board []})

(def winning-combos [#{:top-left :top-middle :top-right}
                     #{:centre-left :centre-middle :centre-right}
                     #{:bottom-left :bottom-middle :bottom-right}
                     #{:top-left :centre-left :bottom-left}
                     #{:top-middle :centre-middle :bottom-middle}
                     #{:top-right :centre-right :bottom-right}
                     #{:top-left :centre-middle :bottom-right}
                     #{:top-right :centre-middle :bottom-left}])

(defn- alternate [last-player]
  (if (= last-player :x) :o :x))

(defn- all-taken? [taken-squares combo]
  (set/subset? combo taken-squares))

(defn- taken-by? [player [_ by]] (= player by))

(defn- taken-squares
  ([board]
   (set (map first board)))
  ([board who]
   (set (map first (filter (partial taken-by? who) board)))))

(defn- won? [board who]
  (some (partial all-taken? (taken-squares board who)) winning-combos))

(defn- full? [board] (= 9 (count board)))

(defn- game-state [board last-player]
  (cond
    (and (= last-player :x) (won? board last-player)) :x-has-won
    (and (= last-player :o) (won? board last-player)) :o-has-won
    (full? board) :stalemate
    :else :game-on))

(defn- over? [state]
  (or (= state :x-has-won)
      (= state :o-has-won)
      (= state :stalemate)))

(defn play [game square]
  (if (over? (game :state))
    game
    (let [{this-player :to-play, previous-board :board} game]
      (if (contains? (taken-squares previous-board) square)
        (assoc game :state :square-already-taken)
        (let [board (conj previous-board [square this-player])
              state (game-state board this-player)
              next-up (if (over? state) :no-one (alternate this-player))]
          (if (over? state)
            (hash-map :state state :board board)
            (hash-map :state state :to-play next-up :board board)))))))