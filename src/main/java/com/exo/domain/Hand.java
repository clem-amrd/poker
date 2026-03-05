package com.exo.domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    
    private final List<Card> cards;

    public Hand(List<Card> holeCards, List<Card> boardCards) {

        List<Card> allCards = this.combine(boardCards, holeCards);

        this.cards = allCards.stream()
                .limit(5)
                .toList();
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<Card> combine(List<Card> board, List<Card> holeCards) {

        List<Card> result = new ArrayList<>();
        result.addAll(board);
        result.addAll(holeCards);

        return result;
    }
}
