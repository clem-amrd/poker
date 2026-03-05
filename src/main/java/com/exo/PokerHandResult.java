package com.exo;

import java.util.List;

public class PokerHandResult {

    private final Category category;
    private final List<Card> chosenCards;

    public PokerHandResult(Category category, List<Card> chosenCards) {
        this.category = category;
        this.chosenCards = chosenCards;
    }

    public Category getCategory() {
        return category;
    }

    public List<Card> getChosenCards() {
        return chosenCards;
    }
}
