(ns tic-tac-toe.core-test
  (:require [midje.sweet :refer :all]
            [tic-tac-toe.core :refer :all]))

(defn after-playing [moves]
  (reduce play new-game moves))

(facts "about a game of tic-tac-toe"
       (fact "x plays first"
             (let [game (after-playing [])]
               (game :state) => :game-on
               (game :to-play) => :x))
       (fact "o plays after x"
             (let [game (after-playing [:top-left])]
               (game :state) => :game-on
               (game :to-play) => :o))
       (fact "the players alternate"
             (let [game (after-playing [:top-left :top-right])]
               (game :state) => :game-on
               (game :to-play) => :x))
       (fact "a square cannot be played twice"
             (let [game (after-playing [:top-left :top-right :top-left])]
               (game :state) => :square-already-taken
               (game :to-play) => :x)))

(facts "about a game that is won"
       (fact "x has three in a line"
             (let [game (after-playing [:top-left :centre-left :top-middle :centre-middle :top-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:centre-left :top-left :centre-middle :top-middle :centre-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:bottom-left :top-left :bottom-middle :top-middle :bottom-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:top-left :top-middle :centre-left :centre-middle :bottom-left])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:top-middle :top-left :centre-middle :centre-left :bottom-middle])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:top-right :top-left :centre-right :centre-left :bottom-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:top-left :bottom-left :centre-middle :top-right :bottom-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)
             (let [game (after-playing [:top-right :top-middle :centre-middle :bottom-right :bottom-left])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one))
       (fact "o has three in a line"
             (let [game (after-playing [:bottom-left :top-left :centre-left :top-middle :centre-middle :top-right])]
               (game :state) => :o-has-won
               (game :to-play) => :no-one))
       (fact "further play is not permitted"
             (let [game (after-playing [:top-left :centre-left :top-middle :centre-middle :top-right :centre-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one))
       (fact "it is not stalemated when won on the ninth play"
             (let [game (after-playing [:top-left
                                        :top-middle
                                        :top-right
                                        :centre-left
                                        :centre-middle
                                        :bottom-left
                                        :centre-right
                                        :bottom-middle
                                        :bottom-right])]
               (game :state) => :x-has-won
               (game :to-play) => :no-one)))

(facts "about a game that is stalemated"
       (fact "the board is full and neither player has a line"
             (let [game (after-playing [:top-left
                                        :top-middle
                                        :top-right
                                        :centre-left
                                        :centre-middle
                                        :bottom-left
                                        :centre-right
                                        :bottom-right
                                        :bottom-middle])]
               (game :state) => :stalemate
               (game :to-play) => :no-one)))