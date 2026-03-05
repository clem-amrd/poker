package com.exo;

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

    
    //comparaison de deux mains (2 joeurs)
    public int compareHands(List<Card> hand1, List<Card> hand2) {

        PokerHandResult result1 = getBestCombination(hand1);
        PokerHandResult result2 = getBestCombination(hand2);

        int categoryCompare = Integer.compare(
                result2.getCategory().ordinal(),
                result1.getCategory().ordinal()
        );
        if (categoryCompare != 0) {
            return categoryCompare;
        }

        return compareSameCategory(hand1, hand2, result1.getCategory());
    }

    //cas ou les deux joueurs ont la meme categorie
    private int compareSameCategory(List<Card> hand1, List<Card> hand2, Category category) {

        switch (category) {
            case STRAIGHT_FLUSH:
                return Integer.compare(
                        getStraightFlushHighValue(hand1),
                        getStraightFlushHighValue(hand2)
                );
            case FOUR_OF_A_KIND:
                return compareFourOfKind(hand1, hand2);
            case FULL_HOUSE:
                return compareFullHouse(hand1, hand2);
            case FLUSH:
                return compareByHighCards(
                        getBestFlushRanks(hand1),
                        getBestFlushRanks(hand2)
                );
            case STRAIGHT:
                return Integer.compare(
                        getStraightHighValue(hand1),
                        getStraightHighValue(hand2)
                );
            case THREE_OF_A_KIND:
                return compareThreeOfKind(hand1, hand2);
            case TWO_PAIR:
                return compareTwoPair(hand1, hand2);
            case ONE_PAIR:
                return compareOnePair(hand1, hand2);
            case HIGH_CARD:
            default:
                return compareByHighCards(
                        getHighCardRanks(hand1),
                        getHighCardRanks(hand2)
                );
        }
    }


    // ------ tous les cas possibles egalité ------


    private int compareFourOfKind(List<Card> hand1, List<Card> hand2) {

        int quad1 = getValueWithCount(hand1, 4);
        int quad2 = getValueWithCount(hand2, 4);
        if (quad1 != quad2) {
            return Integer.compare(quad1, quad2);
        }

        int kicker1 = getHighestExcluding(hand1, List.of(quad1));
        int kicker2 = getHighestExcluding(hand2, List.of(quad2));

        return Integer.compare(kicker1, kicker2);
    }

    private int compareFullHouse(List<Card> hand1, List<Card> hand2) {

        List<Integer> trips1 = getValuesWithCount(hand1, 3);
        List<Integer> trips2 = getValuesWithCount(hand2, 3);

        int trip1 = trips1.get(0);
        int trip2 = trips2.get(0);
        if (trip1 != trip2) {
            return Integer.compare(trip1, trip2);
        }

        int pair1 = getBestPairValueForFullHouse(hand1, trip1);
        int pair2 = getBestPairValueForFullHouse(hand2, trip2);

        return Integer.compare(pair1, pair2);
    }

    private int compareThreeOfKind(List<Card> hand1, List<Card> hand2) {

        int trip1 = getValueWithCount(hand1, 3);
        int trip2 = getValueWithCount(hand2, 3);
        if (trip1 != trip2) {
            return Integer.compare(trip1, trip2);
        }

        List<Integer> kickers1 = getHighCardRanksExcluding(hand1, List.of(trip1), 2);
        List<Integer> kickers2 = getHighCardRanksExcluding(hand2, List.of(trip2), 2);

        return compareByHighCards(kickers1, kickers2);
    }

    private int compareTwoPair(List<Card> hand1, List<Card> hand2) {

        List<Integer> pairs1 = getValuesWithCount(hand1, 2);
        List<Integer> pairs2 = getValuesWithCount(hand2, 2);

        int highPair1 = pairs1.get(0);
        int highPair2 = pairs2.get(0);
        if (highPair1 != highPair2) {
            return Integer.compare(highPair1, highPair2);
        }

        int lowPair1 = pairs1.get(1);
        int lowPair2 = pairs2.get(1);
        if (lowPair1 != lowPair2) {
            return Integer.compare(lowPair1, lowPair2);
        }

        int kicker1 = getHighestExcluding(hand1, List.of(highPair1, lowPair1));
        int kicker2 = getHighestExcluding(hand2, List.of(highPair2, lowPair2));

        return Integer.compare(kicker1, kicker2);
    }

    private int compareOnePair(List<Card> hand1, List<Card> hand2) {

        int pair1 = getValueWithCount(hand1, 2);
        int pair2 = getValueWithCount(hand2, 2);
        if (pair1 != pair2) {
            return Integer.compare(pair1, pair2);
        }

        List<Integer> kickers1 = getHighCardRanksExcluding(hand1, List.of(pair1), 3);
        List<Integer> kickers2 = getHighCardRanksExcluding(hand2, List.of(pair2), 3);

        return compareByHighCards(kickers1, kickers2);
    }

    private int compareByHighCards(List<Integer> ranks1, List<Integer> ranks2) {

        int size = Math.min(ranks1.size(), ranks2.size());
        for (int i = 0; i < size; i++) {
            int a = ranks1.get(i);
            int b = ranks2.get(i);
            if (a != b) {
                return Integer.compare(a, b);
            }
        }
        return Integer.compare(ranks1.size(), ranks2.size());
    }

    private List<Integer> getHighCardRanks(List<Card> cards) {
        return getHighCardRanksExcluding(cards, List.of(), 5);
    }

    private List<Integer> getHighCardRanksExcluding(List<Card> cards, List<Integer> excluded, int limit) {

        return cards.stream()
                .map(c -> c.getValue().getValue())
                .filter(v -> !excluded.contains(v))
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .toList();
    }

    private int getHighestExcluding(List<Card> cards, List<Integer> excluded) {

        return cards.stream()
                .map(c -> c.getValue().getValue())
                .filter(v -> !excluded.contains(v))
                .max(Integer::compareTo)
                .orElse(0);
    }

    private int getValueWithCount(List<Card> cards, int count) {

        return getValuesWithCount(cards, count).get(0);
    }

    private List<Integer> getValuesWithCount(List<Card> cards, int count) {

        return countValues(cards).entrySet().stream()
                .filter(e -> e.getValue() == count)
                .map(e -> e.getKey().getValue())
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    private int getBestPairValueForFullHouse(List<Card> cards, int tripValue) {

        List<Integer> pairs = countValues(cards).entrySet().stream()
                .filter(e -> e.getKey().getValue() != tripValue)
                .filter(e -> e.getValue() >= 2)
                .map(e -> e.getKey().getValue())
                .sorted(Comparator.reverseOrder())
                .toList();

        return pairs.get(0);
    }

    private List<Integer> getBestFlushRanks(List<Card> cards) {

        Map<Suit, List<Card>> bySuit = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        List<Integer> best = List.of();

        for (List<Card> suited : bySuit.values()) {
            if (suited.size() < 5) {
                continue;
            }
            List<Integer> ranks = suited.stream()
                    .map(c -> c.getValue().getValue())
                    .sorted(Comparator.reverseOrder())
                    .limit(5)
                    .toList();

            if (best.isEmpty() || compareByHighCards(ranks, best) > 0) {
                best = ranks;
            }
        }

        return best;
    }

    private int getStraightFlushHighValue(List<Card> cards) {

        Map<Suit, List<Card>> bySuit = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        int best = 0;
        for (List<Card> suited : bySuit.values()) {
            if (suited.size() < 5) {
                continue;
            }
            Integer high = getStraightHighValue(suited);
            if (high != null && high > best) {
                best = high;
            }
        }

        return best;
    }
}
