package com.java.lingvo.domain.dto;

import com.java.lingvo.domain.model.DeckCard;
import com.java.lingvo.domain.model.Card;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DeckCardTest {

    @Test
    void deckCard_basicProperties() {
        DeckCard deckCard = new DeckCard();
        Card card = new Card();
        card.setTerm("Test");
        
        deckCard.setCard(card);
        deckCard.setId(1L);

        assertThat(deckCard.getId()).isEqualTo(1L);
        assertThat(deckCard.getCard().getTerm()).isEqualTo("Test");
    }
}
