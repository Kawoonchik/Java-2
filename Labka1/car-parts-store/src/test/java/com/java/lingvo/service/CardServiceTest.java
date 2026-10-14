package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.mapper.CardMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import com.java.lingvo.domain.dto.CardUpdateRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void createCard_validRequest_savesAndReturnsCard() {
        // Given (Arrange)
        CardCreateRequest request = new CardCreateRequest();
        request.setTerm("語法");
        request.setTranslation("граматика");
        request.setLanguage("Japanese");

        Card card = new Card();
        Card savedCard = new Card();
        CardResponse expectedResponse = new CardResponse();

        when(cardMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(savedCard);
        when(cardMapper.toResponse(savedCard)).thenReturn(expectedResponse);

        // When (Act)
        CardResponse actualResponse = cardService.createCard(request);

        // Then (Assert)
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        // Verify repository interaction
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void getCard_nonExistingId_throwsCardNotFoundException() {
        // Given (Arrange)
        Long nonExistingId = 999L;
        when(cardRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When & Then (Act & Assert)
        assertThrows(CardNotFoundException.class, () -> {
            cardService.getCard(nonExistingId);
        });

        verify(cardRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getCard_existingId_returnsCardResponse() {
        // Given (Arrange)
        Long existingId = 1L;
        Card card = new Card();
        CardResponse expectedResponse = new CardResponse();

        when(cardRepository.findById(existingId)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(expectedResponse);

        // When (Act)
        CardResponse actualResponse = cardService.getCard(existingId);

        // Then (Assert)
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(cardRepository, times(1)).findById(existingId);
    }

    @Test
    void getAllCards_returnsListOfCardResponses() {
        // Given (Arrange)
        Card card = new Card();
        CardResponse response = new CardResponse();

        when(cardRepository.findAll()).thenReturn(List.of(card));
        when(cardMapper.toResponse(card)).thenReturn(response);

        // When (Act)
        List<CardResponse> actualResponses = cardService.getAllCards();

        // Then (Assert)
        assertFalse(actualResponses.isEmpty());
        assertEquals(1, actualResponses.size());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void updateCard_existingId_updatesAndReturnsCard() {
        // Given (Arrange)
        Long existingId = 1L;
        CardUpdateRequest request = new CardUpdateRequest();
        Card existingCard = new Card();
        Card updatedCard = new Card();
        CardResponse expectedResponse = new CardResponse();

        when(cardRepository.findById(existingId)).thenReturn(Optional.of(existingCard));
        when(cardRepository.save(existingCard)).thenReturn(updatedCard);
        when(cardMapper.toResponse(updatedCard)).thenReturn(expectedResponse);

        // When (Act)
        CardResponse actualResponse = cardService.updateCard(existingId, request);

        // Then (Assert)
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(cardRepository, times(1)).save(existingCard);
    }

    @Test
    void deleteCard_existingId_deletesCard() {
        // Given (Arrange)
        Long existingId = 1L;
        when(cardRepository.existsById(existingId)).thenReturn(true);

        // When (Act)
        cardService.deleteCard(existingId);

        // Then (Assert)
        verify(cardRepository, times(1)).deleteById(existingId);
    }

    @Test
    void deleteCard_nonExistingId_throwsCardNotFoundException() {
        // Given
        Long nonExistingId = 999L;
        when(cardRepository.existsById(nonExistingId)).thenReturn(false);

        // When & Then
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(nonExistingId));
        verify(cardRepository, never()).deleteById(anyLong());
    }
}