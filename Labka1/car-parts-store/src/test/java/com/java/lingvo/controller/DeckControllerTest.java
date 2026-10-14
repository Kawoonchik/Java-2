package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.security.CurrentUser;
import com.java.lingvo.service.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckControllerTest {

    @Mock
    private DeckService deckService;

    @InjectMocks
    private DeckController deckController;

    private CurrentUser user(String username) {
        return new CurrentUser(username, List.of("USER"));
    }

    @Test
    void listAll_returnsAllDecks() {
        List<DeckResponse> expected = List.of(new DeckResponse());
        when(deckService.getAllDecks()).thenReturn(expected);

        assertThat(deckController.listAll()).isSameAs(expected);
    }

    @Test
    void create_delegatesUsernameAndRequestToService() {
        CreateDeckRequest request = new CreateDeckRequest();
        DeckResponse expected = new DeckResponse();
        CurrentUser user = user("alice");
        when(deckService.createDeck(request, "alice")).thenReturn(expected);

        assertThat(deckController.create(request, user)).isSameAs(expected);
    }

    @Test
    void myDecks_delegatesUsernameToService() {
        CurrentUser user = user("alice");
        List<DeckResponse> expected = List.of(new DeckResponse());
        when(deckService.getUserDecks("alice")).thenReturn(expected);

        assertThat(deckController.myDecks(user)).isSameAs(expected);
    }

    @Test
    void getById_delegatesToService() {
        DeckResponse expected = new DeckResponse();
        when(deckService.getDeck(1L)).thenReturn(expected);

        assertThat(deckController.getById(1L)).isSameAs(expected);
    }

    @Test
    void update_delegatesToService() {
        CreateDeckRequest request = new CreateDeckRequest();
        DeckResponse expected = new DeckResponse();
        when(deckService.updateDeck(1L, request)).thenReturn(expected);

        assertThat(deckController.update(1L, request)).isSameAs(expected);
    }

    @Test
    void delete_delegatesToService() {
        deckController.delete(1L);
        verify(deckService).deleteDeck(1L);
    }
}
