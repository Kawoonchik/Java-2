package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.mapper.DeckMapper;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.exception.DeckNotFoundException;
import com.java.lingvo.repository.DeckRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.java.lingvo.domain.dto.DeckCardRequest;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.CardRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckMapper deckMapper;

    @InjectMocks
    private DeckService deckService;

    @Test
    void createDeck_validRequest_savesAndReturnsDeck() {
        // Given
        String username = "test-user";
        CreateDeckRequest request = new CreateDeckRequest();
        request.setTitle("New Deck");
        
        DeckCardRequest itemRequest = new DeckCardRequest();
        itemRequest.setCardId(1L);
        request.setItems(List.of(itemRequest));

        Deck deck = new Deck();
        Card card = new Card();
        
        when(deckMapper.toEntity(request)).thenReturn(deck);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(deckRepository.save(deck)).thenReturn(deck);
        when(deckMapper.toResponse(deck)).thenReturn(new DeckResponse());

        // When
        DeckResponse actualResponse = deckService.createDeck(request, username);

        // Then
        assertNotNull(actualResponse);
        assertEquals(username, deck.getCustomerUsername());
        assertFalse(deck.getCards().isEmpty());
        verify(deckRepository, times(1)).save(deck);
    }

    @Test
    void createDeck_cardNotFound_throwsCardNotFoundException() {
        // Given
        CreateDeckRequest request = new CreateDeckRequest();
        DeckCardRequest itemRequest = new DeckCardRequest();
        itemRequest.setCardId(1L);
        request.setItems(List.of(itemRequest));

        Deck deck = new Deck();
        when(deckMapper.toEntity(request)).thenReturn(deck);
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CardNotFoundException.class, () -> deckService.createDeck(request, "user"));
        verify(deckRepository, never()).save(any());
    }

    @Test
    void getUserDecks_returnsDecksForUser() {
        // Given
        String username = "user";
        when(deckRepository.findByCustomerUsername(username)).thenReturn(List.of(new Deck()));
        when(deckMapper.toResponse(any())).thenReturn(new DeckResponse());

        // When
        List<DeckResponse> result = deckService.getUserDecks(username);

        // Then
        assertEquals(1, result.size());
        verify(deckRepository).findByCustomerUsername(username);
    }

    @Test
    void getDeck_existingId_returnsDeck() {
        // Given
        Long id = 1L;
        Deck deck = new Deck();
        when(deckRepository.findById(id)).thenReturn(Optional.of(deck));
        when(deckMapper.toResponse(deck)).thenReturn(new DeckResponse());

        // When
        DeckResponse result = deckService.getDeck(id);

        // Then
        assertNotNull(result);
        verify(deckRepository).findById(id);
    }

    @Test
    void getDeck_nonExistingId_throwsDeckNotFoundException() {
        // Given
        Long nonExistingId = 999L;
        when(deckRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DeckNotFoundException.class, () -> deckService.getDeck(nonExistingId));
        verify(deckRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getAllDecks_returnsAllDecks() {
        // Given
        when(deckRepository.findAll()).thenReturn(List.of(new Deck()));
        when(deckMapper.toResponse(any())).thenReturn(new DeckResponse());

        // When
        List<DeckResponse> result = deckService.getAllDecks();

        // Then
        assertEquals(1, result.size());
        verify(deckRepository).findAll();
    }

    @Test
    void updateDeck_existingId_updatesAndReturnsDeck() {
        // Given
        Long id = 1L;
        CreateDeckRequest request = new CreateDeckRequest();
        request.setTitle("Updated Title");
        request.setItems(Collections.emptyList());

        Deck deck = new Deck();
        when(deckRepository.findById(id)).thenReturn(Optional.of(deck));
        when(deckRepository.save(deck)).thenReturn(deck);
        when(deckMapper.toResponse(deck)).thenReturn(new DeckResponse());

        // When
        DeckResponse result = deckService.updateDeck(id, request);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", deck.getTitle());
        verify(deckRepository).save(deck);
    }

    @Test
    void deleteDeck_existingId_deletesDeck() {
        // Given
        Long id = 1L;
        when(deckRepository.existsById(id)).thenReturn(true);

        // When
        deckService.deleteDeck(id);

        // Then
        verify(deckRepository).deleteById(id);
    }

    @Test
    void deleteDeck_nonExistingId_throwsDeckNotFoundException() {
        // Given
        Long id = 999L;
        when(deckRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(DeckNotFoundException.class, () -> deckService.deleteDeck(id));
        verify(deckRepository, never()).deleteById(anyLong());
    }
}