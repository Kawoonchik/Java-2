package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.domain.model.DeckCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeckMapperImpl.class, DeckCardMapperImpl.class})
class DeckMapperImplTest {

    @Autowired
    private DeckCardMapper deckCardMapper;

    @Autowired
    private DeckMapper deckMapper;

    private Card card(Long id, String term, String translation) {
        Card card = new Card();
        card.setId(id);
        card.setTerm(term);
        card.setTranslation(translation);
        return card;
    }

    @Test
    void deckCardMapper_mapsCardFields() {
        DeckCard item = new DeckCard();
        item.setId(10L);
        item.setCard(card(1L, "語法", "граматика"));

        DeckCardResponse response = deckCardMapper.toResponse(item);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getCardId()).isEqualTo(1L);
        assertThat(response.getTerm()).isEqualTo("語法");
        assertThat(response.getTranslation()).isEqualTo("граматика");
    }

    @Test
    void deckMapper_mapsDeckWithNestedItemsList() {
        Deck deck = new Deck();
        deck.setId(5L);
        deck.setTitle("Japanese Basics");
        deck.setDescription("Basic vocabulary");

        DeckCard item = new DeckCard();
        item.setId(1L);
        item.setCard(card(2L, "こんにちは", "Hello"));
        item.setDeck(deck);
        deck.getCards().add(item);

        DeckResponse response = deckMapper.toResponse(deck);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getTitle()).isEqualTo("Japanese Basics");
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getTerm()).isEqualTo("こんにちは");
    }

    @Test
    void deckMapper_emptyItemsList_mapsToEmptyList() {
        Deck deck = new Deck();
        deck.setId(6L);
        deck.setTitle("Empty Deck");

        DeckResponse response = deckMapper.toResponse(deck);

        assertThat(response.getItems()).isEmpty();
    }
}
