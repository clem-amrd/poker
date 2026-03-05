package com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale.Category;

import org.junit.jupiter.api.Test;

import com.exo.domain.Card;
import com.exo.domain.Suit;
import com.exo.domain.Value;

public class combinaisonTest {

    @Test
    void shouldReturnBestFiveCards() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(5, result.getChosenCards().size());

        List<Card> expected = List.of(
            new Card(Value.ACE, Suit.SPADE),
            new Card(Value.KING, Suit.HEART),
            new Card(Value.QUEEN, Suit.DIAMOND),
            new Card(Value.JACK, Suit.CLUB),
            new Card(Value.TEN, Suit.SPADE)
        );

        assertEquals(expected, result.getChosenCards());
    }

    @Test
    void shouldDetectOnePair() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.NINE, Suit.DIAMOND),
                new Card(Value.THREE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.ONE_PAIR, result.getCategory());

        long aceCount = result.getChosenCards().stream()
                .filter(c -> c.getValue() == Value.ACE)
                .count();

        assertTrue(aceCount == 2);
        assertEquals(5, result.getChosenCards().size());
    }

    @Test
    void shouldDetectTwoPair() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.THREE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.TWO_PAIR, result.getCategory());
        assertEquals(5, result.getChosenCards().size());

        long aceCount = result.getChosenCards().stream()
                .filter(c -> c.getValue() == Value.ACE)
                .count();

        long kingCount = result.getChosenCards().stream()
                .filter(c -> c.getValue() == Value.KING)
                .count();

        assertTrue(aceCount >= 2);
        assertTrue(kingCount >= 2);
    }

    @Test
    void shouldDetectThreeOfKind() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.THREE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.THREE_OF_A_KIND, result.getCategory());
        assertEquals(5, result.getChosenCards().size());

        long aceThreeOfKind = result.getChosenCards().stream()
            .filter(c -> c.getValue() == Value.ACE)
            .count();

        assertTrue(aceThreeOfKind == 3);
    }

    @Test
    void shouldDetectStraight() {

        List<Card> cards = List.of(
                new Card(Value.TEN, Suit.SPADE),
                new Card(Value.JACK, Suit.HEART),
                new Card(Value.QUEEN, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.STRAIGHT, result.getCategory());
        assertEquals(5, result.getChosenCards().size());
    }

    @Test
    void shouldDetectFlush() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.QUEEN, Suit.HEART),
                new Card(Value.JACK, Suit.HEART),
                new Card(Value.NINE, Suit.HEART),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.FLUSH, result.getCategory());
        assertEquals(5, result.getChosenCards().size());
    }
    
    @Test
    void shouldDetectFullHouse() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.KING, Suit.DIAMOND),
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.FULL_HOUSE, result.getCategory());
        assertEquals(5, result.getChosenCards().size());
    }

    @Test
    void shouldDetectFourOfKind() {

        List<Card> cards = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.ACE, Suit.HEART),
                new Card(Value.ACE, Suit.CLUB),
                new Card(Value.ACE, Suit.DIAMOND),
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.FOUR_OF_A_KIND, result.getCategory());
        assertEquals(5, result.getChosenCards().size());
    }

    @Test
    void shouldDetectStraightFlush() {

        List<Card> cards = List.of(
                new Card(Value.TEN, Suit.SPADE),
                new Card(Value.JACK, Suit.SPADE),
                new Card(Value.QUEEN, Suit.SPADE),
                new Card(Value.KING, Suit.SPADE),
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.THREE, Suit.HEART),
                new Card(Value.FOUR, Suit.CLUB)
        );

        PokerHandCombinaison combinaison = new PokerHandCombinaison();

        PokerHandResult result = combinaison.getBestCombination(cards);

        assertEquals(Category.STRAIGHT_FLUSH, result.getCategory());
        assertEquals(5, result.getChosenCards().size());
    }
}
