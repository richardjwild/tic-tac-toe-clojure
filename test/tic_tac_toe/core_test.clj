(ns tic-tac-toe.core-test
  (:require [midje.sweet :refer :all]
            [tic-tac-toe.core :refer :all]))

(defn after-playing [& square]
  (reduce play new-game square))

(def stalemated-game (after-playing :top-left
                                    :top-middle
                                    :top-right
                                    :centre-left
                                    :centre-middle
                                    :bottom-left
                                    :centre-right
                                    :bottom-right
                                    :bottom-middle))

(def game-won-with-full-board (after-playing :top-left
                                             :top-middle
                                             :top-right
                                             :centre-left
                                             :centre-middle
                                             :bottom-left
                                             :centre-right
                                             :bottom-middle
                                             :bottom-right))

(facts "about a game of tic tac toe"
       (fact "x plays first"
             (:state new-game) => :game-on
             (:to-play new-game) => :x)
       (fact "o plays after x"
             (let [game (after-playing :top-left)]
               (:state game) => :game-on
               (:to-play game) => :o))
       (fact "players alternate"
             (let [game (after-playing :top-left :top-middle)]
               (:state game) => :game-on
               (:to-play game) => :x))
       (fact "same square cannot be played twice"
             (let [game (after-playing :top-left :top-middle :top-left)]
               (:state game) => :square-already-played
               (:to-play game) => :x)))

(facts "about a stalemated game"
       (fact "the board is full"
             (let [game stalemated-game]
               (:state game) => :stalemate))
       (fact "no-one is to play next"
             (let [game stalemated-game]
               (contains? game :to-play) => false))
       (fact "further play is not permitted"
             (let [game (play stalemated-game :top-left)]
               (:state game) => :stalemate))
       (fact "neither player has three in a row"
             (let [game game-won-with-full-board]
               (:state game) =deny=> :stalemate)))

(facts "about a game won by x"
       (fact "they have three in a row"
             (let [game (after-playing :top-left :centre-left :top-middle :centre-middle :top-right)]
               (:state game) => :x-has-won)
             (let [game (after-playing :centre-left :top-left :centre-middle :top-middle :centre-right)]
               (:state game) => :x-has-won)
             (let [game (after-playing :bottom-left :top-left :bottom-middle :top-middle :bottom-right)]
               (:state game) => :x-has-won)
             (let [game (after-playing :top-left :top-right :centre-left :centre-right :bottom-left)]
               (:state game) => :x-has-won)
             (let [game (after-playing :top-middle :top-right :centre-middle :centre-right :bottom-middle)]
               (:state game) => :x-has-won)
             (let [game (after-playing :top-right :top-left :centre-right :centre-left :bottom-right)]
               (:state game) => :x-has-won)
             (let [game (after-playing :top-left :centre-left :centre-middle :top-middle :bottom-right)]
               (:state game) => :x-has-won)
             (let [game (after-playing :bottom-left :centre-left :centre-middle :bottom-middle :top-right)]
               (:state game) => :x-has-won))
       (fact "no-one is to play next"
             (let [game (after-playing :top-left :centre-left :top-middle :centre-middle :top-right)]
               (contains? game :to-play) => false))
       (fact "further play is not permitted"
             (let [game (after-playing :top-left :centre-left :top-middle :centre-middle :top-right :centre-right)]
               (:state game) => :x-has-won)))

(facts "about a game won by o"
       (fact "they have three in a row"
             (let [game (after-playing :bottom-left :top-left :centre-left :top-middle :centre-middle :top-right)]
               (:state game) => :o-has-won)
             (let [game (after-playing :bottom-left :centre-left :top-left :centre-middle :top-middle :centre-right)]
               (:state game) => :o-has-won)
             (let [game (after-playing :centre-left :bottom-left :top-left :bottom-middle :top-middle :bottom-right)]
               (:state game) => :o-has-won)
             (let [game (after-playing :top-middle :top-left :top-right :centre-left :centre-right :bottom-left)]
               (:state game) => :o-has-won)
             (let [game (after-playing :top-left :top-middle :top-right :centre-middle :centre-right :bottom-middle)]
               (:state game) => :o-has-won)
             (let [game (after-playing :top-middle :top-right :top-left :centre-right :centre-left :bottom-right)]
               (:state game) => :o-has-won)
             (let [game (after-playing :bottom-middle :top-left :centre-left :centre-middle :top-middle :bottom-right)]
               (:state game) => :o-has-won)
             (let [game (after-playing :centre-right :bottom-left :centre-left :centre-middle :bottom-middle :top-right)]
               (:state game) => :o-has-won))
       (fact "no-one is to play next"
             (let [game (after-playing :bottom-left :centre-left :top-middle :centre-middle :top-right :centre-right)]
               (contains? game :to-play) => false))
       (fact "further play is not permitted"
             (let [game (after-playing :bottom-left :top-left :centre-left :top-middle :centre-middle :top-right :centre-right)]
               (:state game) => :o-has-won)))