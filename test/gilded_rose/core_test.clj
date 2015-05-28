(ns gilded-rose.core-test
  (:use midje.sweet)
  (:require [gilded-rose.core :use [item update-quality]]))

(fact "a normal item is normal"
      (let [inventory [(item "something" 2 2)]
        result (update-quality inventory)
        updated-item (first result)]
        (fact "sell-in decreases"
              (:sell-in updated-item) => 1)
        (fact "quality decreases"
              (:quality updated-item) => 1)))

