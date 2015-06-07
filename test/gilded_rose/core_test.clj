(ns gilded-rose.core-test
  (:use midje.sweet)
  (:require [gilded-rose.core :use [item update-quality]]))

(defn single-item-runner [single-item]
  (let [inventory [single-item]
        result (update-quality inventory)]
        (first result)))

(defn make-uniform-inventory [sell-in quality]
  (let [normal (item "normal" sell-in quality)
        cheese (item "Aged Brie" sell-in quality)
        sulfur (item "Sulfuras" sell-in quality)
        passes (item "Backstage passes" sell-in quality)]
    [normal cheese sulfur passes]))



;(fact "a normal item is normal"
;      (let [inventory [(item "something" 2 2)]
;        result (update-quality inventory)
;        updated-item (first result)]
;        (fact "sell-in decreases"
;              (:sell-in updated-item) => 1)))
;        (fact "quality decreases"
;              (:quality updated-item) => 1)))


;(fact "a normal item "
;      (let [test-item (item "normal" 10 10)
;            result (single-item-runner test-item)]
;        (fact "sell-in decrements by 1"
;              (:sell-in result) => 9)
;        (fact "quality decrements by 1"
;              (:quality result) => 9)))

(fact "quality doesn't go negative"
      (let [inventory (make-uniform-inventory 0 0)
            result (update-quality inventory)
            qualities (map :quality result)]
        qualities => (has not-any? neg?)
        (count qualities) => 4))

;(fact "sell-in decrements by 1"
;      (let [inventory (make-uniform-inventory 1 0)]

