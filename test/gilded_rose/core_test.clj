(ns gilded-rose.core-test
  (:use midje.sweet)
  (:require [gilded-rose.core :use [item update-quality]]))

(def sulfuras-name "Sulfuras, Hand of Ragnaros")

(defn single-item-runner [single-item]
  (let [inventory [single-item]
        result (update-quality inventory)]
        (first result)))

(defn make-uniform-inventory [sell-in quality]
  (let [normal (item "normal" sell-in quality)
        cheese (item "Aged Brie" sell-in quality)
        sulfur (item sulfuras-name sell-in quality)
        passes (item "Backstage passes" sell-in quality)]
    [normal cheese sulfur passes]))


(fact "quality doesn't go negative"
      (let [inventory (make-uniform-inventory 0 0)
            result (update-quality inventory)
            qualities (map :quality result)]
        qualities => (has not-any? neg?)
        (count qualities) => 4))

(fact "sell-in decreases each day except sulfuras"
      (let [inventory (make-uniform-inventory 1 1)
            non-sulf-inv (remove (fn [x] (= (:name x) sulfuras-name)) inventory)
            result (update-quality non-sulf-inv)
            sellins (map :sell-in result)]
        sellins => (has every? zero?)
        (count sellins) => 3))

(fact "sulfuras "
      (let [sulfuras (item sulfuras-name 0 80)
            result (single-item-runner sulfuras)]
        (fact "sell-in stays the same"
              (:sell-in result) => 0)
        (fact "quality stays the same"
              (:quality result) => 80)))






;(fact "a normal item is normal
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


;(fact "sell-in decrements by 1"
;      (let [inventory (make-uniform-inventory 1 0)]

