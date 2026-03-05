package com.exo.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokerHandCombinaison {

    public PokerHandResult getBestCombination(List<Card> cards) {

        // tri cartes
        List<Card> sorted = cards.stream()
                .sorted((a, b) -> b.getValue().getValue() - a.getValue().getValue())
                .collect(Collectors.toList());

        // Straight Flush
        List<Card> straightFlush = getStraightFlushBest(cards);
        if (straightFlush != null) {
            return new PokerHandResult(
                    Category.STRAIGHT_FLUSH,
                    straightFlush
            );
        }

        // Four of a kind
        Map<Value, Long> counts = countValues(cards);

        if (counts.containsValue(4L)) {

            Value quadValue = counts.entrySet().stream()
                    .filter(e -> e.getValue() == 4)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .get();

            List<Card> chosen = cards.stream()
                    .filter(c -> c.getValue() == quadValue)
                    .collect(Collectors.toList());

            Card kicker = sorted.stream()
                    .filter(c -> c.getValue() != quadValue)
                    .findFirst().get();

            chosen.add(kicker);

            return new PokerHandResult(Category.FOUR_OF_A_KIND, chosen);
        }

        // Full house
        if (counts.containsValue(3L) && counts.containsValue(2L)) {

            Value three = counts.entrySet().stream()
                    .filter(e -> e.getValue() == 3)
                    .map(Map.Entry::getKey)
                    .findFirst().get();

            Value pair = counts.entrySet().stream()
                    .filter(e -> e.getValue() == 2)
                    .map(Map.Entry::getKey)
                    .findFirst().get();

            List<Card> chosen = cards.stream()
                    .filter(c -> c.getValue() == three || c.getValue() == pair)
                    .limit(5)
                    .collect(Collectors.toList());

            return new PokerHandResult(Category.FULL_HOUSE, chosen);
        }

        // Flush
        List<Card> bestFlush = getBestFlushCards(cards);
        if (bestFlush != null) {
            return new PokerHandResult(
                    Category.FLUSH,
                    bestFlush
            );
        }

        // Straight
        if (isStraight(cards)) {
            return new PokerHandResult(
                    Category.STRAIGHT,
                    getStraightBest(cards)
            );
        }

        // Three of kind
        if (counts.containsValue(3L)) {

            Value three = counts.entrySet().stream()
                    .filter(e -> e.getValue() == 3)
                    .map(Map.Entry::getKey)
                    .findFirst().get();

            List<Card> chosen = cards.stream()
                    .filter(c -> c.getValue() == three)
                    .collect(Collectors.toList());

            chosen.addAll(
                    sorted.stream()
                            .filter(c -> c.getValue() != three)
                            .limit(2)
                            .toList()
            );

            return new PokerHandResult(Category.THREE_OF_A_KIND, chosen);
        }

        // Two pair
        List<Value> pairs = counts.entrySet().stream()
                .filter(e -> e.getValue() == 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(Value::getValue).reversed())
                .toList();

        if (pairs.size() >= 2) {

            List<Card> chosen = new ArrayList<>();

            Value pair1 = pairs.get(0);
            Value pair2 = pairs.get(1);

            chosen.addAll(cards.stream()
                    .filter(c -> c.getValue() == pair1 || c.getValue() == pair2)
                    .toList());

            chosen.add(sorted.stream()
                    .filter(c -> c.getValue() != pair1 && c.getValue() != pair2)
                    .findFirst().get());

            return new PokerHandResult(Category.TWO_PAIR, chosen);
        }

        // One pair
        if (pairs.size() == 1) {

            Value pair = pairs.get(0);

            List<Card> chosen = cards.stream()
                    .filter(c -> c.getValue() == pair)
                    .collect(Collectors.toList());

            chosen.addAll(
                    sorted.stream()
                            .filter(c -> c.getValue() != pair)
                            .limit(3)
                            .toList()
            );

            return new PokerHandResult(Category.ONE_PAIR, chosen);
        }

        return new PokerHandResult(
                Category.HIGH_CARD,
                sorted.stream().limit(5).toList()
        );
    }


    private boolean isStraight(List<Card> cards) {
        return getStraightHighValue(cards) != null;
    }

    private List<Card> getStraightBest(List<Card> cards) {
        Integer high = getStraightHighValue(cards);
        if (high == null) {
            return List.of();
        }

        List<Integer> needed = new ArrayList<>();
        if (high == 5 && hasWheelStraight(cards)) {
            needed = List.of(5, 4, 3, 2, 1);
        } else {
            for (int v = high; v >= high - 4; v--) {
                needed.add(v);
            }
        }

        List<Card> chosen = new ArrayList<>();
        for (int v : needed) {
            int target = (v == 1) ? 14 : v;
            Card pick = cards.stream()
                    .filter(c -> c.getValue().getValue() == target)
                    .findFirst()
                    .orElseThrow();
            chosen.add(pick);
        }

        return chosen;
    }

    private Map<Value, Long> countValues(List<Card> cards) {

        return cards.stream()
                .collect(Collectors.groupingBy(
                        Card::getValue,
                        Collectors.counting()
                ));
    }

    private List<Card> getStraightFlushBest(List<Card> cards) {

        Map<Suit, List<Card>> bySuit = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        List<Card> best = null;
        Integer bestHigh = null;

        for (Map.Entry<Suit, List<Card>> entry : bySuit.entrySet()) {
            List<Card> suited = entry.getValue();
            if (suited.size() < 5) {
                continue;
            }
            Integer high = getStraightHighValue(suited);
            if (high == null) {
                continue;
            }
            if (bestHigh == null || high > bestHigh) {
                bestHigh = high;
                best = getStraightBest(suited);
            }
        }

        return best;
    }

    private List<Card> getBestFlushCards(List<Card> cards) {

        Map<Suit, List<Card>> bySuit = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        List<Card> best = null;

        for (List<Card> suited : bySuit.values()) {
            if (suited.size() < 5) {
                continue;
            }
            List<Card> sorted = suited.stream()
                    .sorted((a, b) -> b.getValue().getValue() - a.getValue().getValue())
                    .limit(5)
                    .toList();
            if (best == null) {
                best = sorted;
            } else {
                int currentHigh = sorted.get(0).getValue().getValue();
                int bestHigh = best.get(0).getValue().getValue();
                if (currentHigh > bestHigh) {
                    best = sorted;
                }
            }
        }

        return best;
    }

    private Integer getStraightHighValue(List<Card> cards) {

        List<Integer> values = cards.stream()
                .map(c -> c.getValue().getValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (values.contains(14)) {
            values.add(1);
            values = values.stream().distinct().sorted().toList();
        }

        int run = 1;
        Integer bestHigh = null;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) - values.get(i - 1) == 1) {
                run++;
            } else {
                run = 1;
            }
            if (run >= 5) {
                bestHigh = values.get(i);
            }
        }

        return bestHigh;
    }

    private boolean hasWheelStraight(List<Card> cards) {
        List<Integer> values = cards.stream()
                .map(c -> c.getValue().getValue())
                .distinct()
                .sorted()
                .toList();
        return values.containsAll(List.of(14, 2, 3, 4, 5));
    }
}
