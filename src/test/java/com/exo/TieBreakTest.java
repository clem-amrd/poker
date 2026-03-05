package com.exo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TieBreakTest {
    @Test
    void shouldBreakTieBetweenTwoStraights() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> straightHigh = List.of(
                new Card(Value.TEN, Suit.SPADE),
                new Card(Value.JACK, Suit.HEART),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> straightLow = List.of(
                new Card(Value.NINE, Suit.SPADE),
                new Card(Value.EIGHT, Suit.HEART),
                new Card(Value.SEVEN, Suit.DIAMOND),
                new Card(Value.SIX, Suit.CLUB),
                new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(straightHigh, straightLow);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoFlushesByHighCard() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> flushAceHigh = List.of(
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.NINE, Suit.HEART),
                new Card(Value.SEVEN, Suit.HEART),
                new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.CLUB),
                new Card(Value.FOUR, Suit.SPADE)
        );

        List<Card> flushQueenHigh = List.of(
                new Card(Value.QUEEN, Suit.HEART),
                new Card(Value.JACK, Suit.HEART),
                new Card(Value.NINE, Suit.HEART),
                new Card(Value.SEVEN, Suit.HEART),
                new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.CLUB),
                new Card(Value.FOUR, Suit.SPADE)
        );

        int result = combinaison.compareHands(flushAceHigh, flushQueenHigh);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoTwoPairsBySecondPair() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> acesAndQueens = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.QUEEN, Suit.CLUB),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.NINE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> acesAndJacks = List.of(
                new Card(Value.ACE, Suit.DIAMOND),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.JACK, Suit.SPADE),
                new Card(Value.JACK, Suit.HEART),
                new Card(Value.NINE, Suit.DIAMOND),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(acesAndQueens, acesAndJacks);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoPairsByPairRank() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> pairOfAces = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.JACK, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> pairOfKings = List.of(
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.JACK, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(pairOfAces, pairOfKings);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoThreeOfAKindByTripRank() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> tripAces = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> tripKings = List.of(
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.ACE, Suit.DIAMOND),
                new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(tripAces, tripKings);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoFullHousesByTripRank() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> acesOverKings = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> kingsOverAces = List.of(
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.ACE, Suit.DIAMOND),
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(acesOverKings, kingsOverAces);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoFourOfAKindByQuadRank() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> quadAces = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.ACE, Suit.DIAMOND),
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> quadKings = List.of(
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        int result = combinaison.compareHands(quadAces, quadKings);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoStraightFlushes() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> straightFlushNineHigh = List.of(
                new Card(Value.NINE, Suit.SPADE),
                new Card(Value.EIGHT, Suit.SPADE),
                new Card(Value.SEVEN, Suit.SPADE),
                new Card(Value.SIX, Suit.SPADE),
                new Card(Value.FIVE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.CLUB)
        );

        List<Card> straightFlushFiveHigh = List.of(
                new Card(Value.FIVE, Suit.HEART),
                new Card(Value.FOUR, Suit.HEART),
                new Card(Value.THREE, Suit.HEART),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.QUEEN, Suit.CLUB)
        );

        int result = combinaison.compareHands(straightFlushNineHigh, straightFlushFiveHigh);

        assertTrue(result > 0);
    }

    @Test
    void shouldBreakTieBetweenTwoHighCardHands() {

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        List<Card> aceHigh = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.NINE, Suit.DIAMOND),
                new Card(Value.SEVEN, Suit.CLUB),
                new Card(Value.THREE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        List<Card> kingHigh = List.of(
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.QUEEN, Suit.HEART),
                new Card(Value.NINE, Suit.DIAMOND),
                new Card(Value.SEVEN, Suit.CLUB),
                new Card(Value.THREE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        int result = combinaison.compareHands(aceHigh, kingHigh);

        assertTrue(result > 0);
    }
}
