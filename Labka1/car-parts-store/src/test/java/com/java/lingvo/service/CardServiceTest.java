package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.mapper.CardMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private Card product(int id) {
        Card card = new Card();
        card.setId(id);
        card.setSku("SKU-" + id);
        card.setName("Product " + id);
        card.setCategory("ENGINE");
        card.setManufacturer("Bosch");
        card.setPrice(new BigDecimal("10.00"));
        card.setStock(5);
        return card;
    }

    @Test
    void listAll_mapsEveryProduct() {
        Card p1 = product(1);
        Card p2 = product(2);
        CardResponse r1 = new CardResponse();
        CardResponse r2 = new CardResponse();
        when(cardRepository.findAll()).thenReturn(List.of(p1, p2));
        when(cardMapper.toResponse(p1)).thenReturn(r1);
        when(cardMapper.toResponse(p2)).thenReturn(r2);

        List<CardResponse> result = cardService.listAll();

        assertThat(result).containsExactly(r1, r2);
    }

    @Test
    void listAll_noProducts_returnsEmptyList() {
        when(cardRepository.findAll()).thenReturn(List.of());

        assertThat(cardService.listAll()).isEmpty();
    }

    @Test
    void getById_productExists_returnsMappedResponse() {
        Card card = product(1);
        CardResponse response = new CardResponse();
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(response);

        assertThat(cardService.getById(1)).isSameAs(response);
    }

    @Test
    void getById_productMissing_throwsProductNotFoundException() {
        when(cardRepository.findById(404)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.getById(404))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessageContaining("404");
    }

    @Test
    void create_mapsRequestToEntityAndSaves() {
        CardCreateRequest request = new CardCreateRequest(
                "SKU-9", "New Part", "ENGINE", "Bosch", null, new BigDecimal("12.00"), 10);
        Card entity = product(9);
        Card saved = product(9);
        CardResponse response = new CardResponse();
        when(cardMapper.toEntity(request)).thenReturn(entity);
        when(cardRepository.save(entity)).thenReturn(saved);
        when(cardMapper.toResponse(saved)).thenReturn(response);

        assertThat(cardService.create(request)).isSameAs(response);
        verify(cardRepository).save(entity);
    }

    @Test
    void update_productExists_appliesChangesAndSaves() {
        Card existing = product(1);
        CardUpdateRequest request = new CardUpdateRequest(
                "Updated Name", "BRAKES", "NGK", "Honda Civic", new BigDecimal("20.00"), 3);
        CardResponse response = new CardResponse();
        when(cardRepository.findById(1)).thenReturn(Optional.of(existing));
        when(cardRepository.save(existing)).thenReturn(existing);
        when(cardMapper.toResponse(existing)).thenReturn(response);

        CardResponse result = cardService.update(1, request);

        verify(cardMapper).updateEntityFromRequest(request, existing);
        assertThat(result).isSameAs(response);
    }

    @Test
    void update_productMissing_throwsProductNotFoundException_beforeTouchingMapper() {
        when(cardRepository.findById(404)).thenReturn(Optional.empty());

        CardUpdateRequest request = new CardUpdateRequest(
                "Name", "CAT", "Manu", null, new BigDecimal("1.00"), 1);

        assertThatThrownBy(() -> cardService.update(404, request))
                .isInstanceOf(CardNotFoundException.class);

        verifyNoInteractions(cardMapper);
    }

    @Test
    void delete_productExists_deletesById() {
        when(cardRepository.existsById(1)).thenReturn(true);

        cardService.delete(1);

        verify(cardRepository).deleteById(1);
    }

    @Test
    void delete_productMissing_throwsProductNotFoundException_andNeverCallsDelete() {
        when(cardRepository.existsById(404)).thenReturn(false);

        assertThatThrownBy(() -> cardService.delete(404))
                .isInstanceOf(CardNotFoundException.class);

        verify(cardRepository, never()).deleteById(any());
    }
}
