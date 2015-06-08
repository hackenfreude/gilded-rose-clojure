(ns gilded-rose.core-test
  (:use midje.sweet)
  (:require [gilded-rose.core :use [item update-quality]]))

(def sulfuras-name "Sulfuras, Hand of Ragnaros")
(def passes-name "Backstage passes to a TAFKAL80ETC concert")
(def cheese-name "Aged Brie")

(defn single-item-runner [single-item]
  (let [inventory [single-item]
        result (update-quality inventory)]
        (first result)))

(defn make-uniform-inventory [sell-in quality]
  (let [normal (item "normal" sell-in quality)
        cheese (item cheese-name sell-in quality)
        sulfur (item sulfuras-name sell-in quality)
        passes (item passes-name sell-in quality)]
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

(fact "aged brie quality increases"
      (let [cheese (item cheese-name 0 0)
            result (single-item-runner cheese)]
        (:quality result) => 1))

(fact "backstage passes "
      (let [passes11 (item passes-name 11 0)
            passes10 (item passes-name 10 0)
            passes6 (item passes-name 6 0)
            passes5 (item passes-name 5 0)
            passes1 (item passes-name 1 0)
            passes0 (item passes-name 0 0)]
          (fact "passes with 11 days quality up by 1"
              (:quality (single-item-runner passes11)) => 1)
          (fact "passes with 10 days quality up by 2"
              (:quality (single-item-runner passes10)) => 2)
          (fact "passes with 6 days quality up by 2"
              (:quality (single-item-runner passes6)) => 2)
          (fact "passes with 5 days quality up by 3"
              (:quality (single-item-runner passes5)) => 3)
          (fact "passes with 1 days quality up by 3"
              (:quality (single-item-runner passes1)) => 3)
          (fact "passes with 0 days quality stays 0"
              (:quality (single-item-runner passes0)) => 0)))
            
(fact "a normal item "
      (let [test-item (item "normal" 10 10)
            result (single-item-runner test-item)]
        (fact "sell-in decrements by 1"
              (:sell-in result) => 9)
        (fact "quality decrements by 1"
              (:quality result) => 9)))

(fact "quality never above 50 except for sulfuras"
      (let [inventory (make-uniform-inventory 0 50)
            non-sulf-inv (remove (fn [x] (= (:name x) sulfuras-name)) inventory)
            result (update-quality non-sulf-inv)
            qualities (map :quality result)]
        qualities => (has every? (fn [x] (>= 50 x)))
        (count qualities) => 3))
