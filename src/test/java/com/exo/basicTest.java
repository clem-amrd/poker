package com.exo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.exo.domain.Card;
import com.exo.domain.Hand;
import com.exo.domain.Suit;
import com.exo.domain.Value;
import java.util.List;
import org.junit.jupiter.api.Test;

public class basicTest{
    
    @Test
    void shouldCreateCardWithValueAndSuit() {

        // Given
        Card card = new Card(Value.ACE, Suit.SPADE);

        // When
        Value value = card.getValue();
        Suit suit = card.getSuit();

        // Then
        assertEquals(Value.ACE, value);
        assertEquals(Suit.SPADE, suit);
    }

    @Test
    void ShouldHaveFiveCardsInHand() {

        // Given
        List<Card> holeCards = List.of(
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        List<Card> board = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.SPADE)
        );

        // When
        Hand hand = new Hand(holeCards, board);

        // Then
        assertEquals(5, hand.getCards().size());
    }

    @Test
    void shouldCombineHoleCardsAndBoardCards() {
        
        // Given
        List<Card> board = List.of(
                new Card(Value.ACE, Suit.SPADE),
                new Card(Value.KING, Suit.HEART),
                new Card(Value.QUEEN, Suit.DIAMOND),
                new Card(Value.JACK, Suit.CLUB),
                new Card(Value.TEN, Suit.SPADE)
        );

        List<Card> holeCards = List.of(
                new Card(Value.TWO, Suit.HEART),
                new Card(Value.THREE, Suit.CLUB)
        );

        // When
        Hand hand = new Hand(holeCards, board);
        List<Card> allCards = hand.combine(board, holeCards);

        // Then
        assertEquals(7, allCards.size());
        assertTrue(allCards.containsAll(board));
        assertTrue(allCards.containsAll(holeCards));
    }

}
