package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    private CardController controller() {
        return new CardController(cardService);
    }

    @Test
    void listAll_returnsWhatServiceReturns() {
        List<CardResponse> expected = List.of(new CardResponse());
        when(cardService.listAll()).thenReturn(expected);

        assertThat(controller().listAll()).isSameAs(expected);
    }

    @Test
    void getById_delegatesWithGivenId() {
        CardResponse expected = new CardResponse();
        when(cardService.getById(7)).thenReturn(expected);

        assertThat(controller().getById(7)).isSameAs(expected);
    }

    @Test
    void create_delegatesRequestToService() {
        CardCreateRequest request = new CardCreateRequest(
                "SKU", "Name", "CAT", "Manu", null, new BigDecimal("1.00"), 1);
        CardResponse expected = new CardResponse();
        when(cardService.create(request)).thenReturn(expected);

        assertThat(controller().create(request)).isSameAs(expected);
    }

    @Test
    void update_delegatesIdAndRequestToService() {
        CardUpdateRequest request = new CardUpdateRequest(
                "Name", "CAT", "Manu", null, new BigDecimal("1.00"), 1);
        CardResponse expected = new CardResponse();
        when(cardService.update(3, request)).thenReturn(expected);

        assertThat(controller().update(3, request)).isSameAs(expected);
    }

    @Test
    void delete_delegatesIdToService() {
        controller().delete(5);

        verify(cardService).delete(5);
    }
}
