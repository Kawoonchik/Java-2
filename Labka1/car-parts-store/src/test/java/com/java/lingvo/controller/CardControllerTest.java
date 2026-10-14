package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @Test
    void listAll_returnsWhatServiceReturns() {
        List<CardResponse> expected = List.of(new CardResponse());
        when(cardService.getAllCards()).thenReturn(expected);

        assertThat(cardController.listAll()).isSameAs(expected);
    }

    @Test
    void getById_delegatesWithGivenId() {
        CardResponse expected = new CardResponse();
        when(cardService.getCard(7L)).thenReturn(expected);

        assertThat(cardController.getById(7L)).isSameAs(expected);
    }

    @Test
    void create_delegatesRequestToService() {
        CardCreateRequest request = new CardCreateRequest();
        CardResponse expected = new CardResponse();
        when(cardService.createCard(request)).thenReturn(expected);

        assertThat(cardController.create(request)).isSameAs(expected);
    }

    @Test
    void update_delegatesIdAndRequestToService() {
        CardUpdateRequest request = new CardUpdateRequest();
        CardResponse expected = new CardResponse();
        when(cardService.updateCard(3L, request)).thenReturn(expected);

        assertThat(cardController.update(3L, request)).isSameAs(expected);
    }

    @Test
    void delete_delegatesIdToService() {
        cardController.delete(123L);

        verify(cardService).deleteCard(123L);
    }
}
