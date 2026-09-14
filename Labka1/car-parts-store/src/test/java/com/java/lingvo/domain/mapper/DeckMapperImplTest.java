package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.enums.OrderStatus;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.domain.model.DeckCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeckMapperImpl.class, DeckCardMapperImpl.class})
class DeckMapperImplTest {

    @Autowired
    private DeckCardMapper deckCardMapper;

    @Autowired
    private DeckMapper deckMapper;

    private Card product(int id, String name) {
        Card card = new Card();
        card.setId(id);
        card.setName(name);
        return card;
    }

    @Test
    void orderItemMapper_mapsProductFieldsAndComputesLineTotal() {
        DeckCard item = new DeckCard();
        item.setCard(product(1, "Brake Pad"));
        item.setQuantity(3);
        item.setUnitPrice(new BigDecimal("10.00"));

        DeckCardResponse response = deckCardMapper.toResponse(item);

        assertThat(response.getProductId()).isEqualTo(1);
        assertThat(response.getProductName()).isEqualTo("Brake Pad");
        assertThat(response.getQuantity()).isEqualTo(3);
        assertThat(response.getUnitPrice()).isEqualByComparingTo("10.00");
        assertThat(response.getLineTotal()).isEqualByComparingTo("30.00");
    }

    @Test
    void orderMapper_mapsOrderWithNestedItemsList() {
        Deck deck = new Deck();
        deck.setId(5);
        deck.setTotalPrice(new BigDecimal("50.00"));
        deck.setStatus(OrderStatus.CONFIRMED);
        deck.setCreatedAt(OffsetDateTime.parse("2026-01-01T10:00:00Z"));

        DeckCard item = new DeckCard();
        item.setCard(product(2, "Oil Filter"));
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("25.00"));
        deck.addItem(item);

        DeckResponse response = deckMapper.toResponse(deck);

        assertThat(response.getId()).isEqualTo(5);
        assertThat(response.getTotalPrice()).isEqualByComparingTo("50.00");
        assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().getFirst().getProductName()).isEqualTo("Oil Filter");
        assertThat(response.getItems().getFirst().getLineTotal()).isEqualByComparingTo("50.00");
    }

    @Test
    void orderMapper_emptyItemsList_mapsToEmptyList() {
        Deck deck = new Deck();
        deck.setId(6);
        deck.setTotalPrice(BigDecimal.ZERO);
        deck.setStatus(OrderStatus.PLACED);
        deck.setCreatedAt(OffsetDateTime.now());

        DeckResponse response = deckMapper.toResponse(deck);

        assertThat(response.getItems()).isEmpty();
    }
}
