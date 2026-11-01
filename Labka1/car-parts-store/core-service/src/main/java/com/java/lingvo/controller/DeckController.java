package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.security.CurrentUser;
import com.java.lingvo.service.DeckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @GetMapping
    public List<DeckResponse> listAll() {
        return deckService.getAllDecks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckResponse create(@Valid @RequestBody CreateDeckRequest request,
                               @AuthenticationPrincipal CurrentUser user) {
        return deckService.createDeck(request, user.username());
    }

    @GetMapping("/mine")
    public List<DeckResponse> myDecks(@AuthenticationPrincipal CurrentUser user) {
        return deckService.getUserDecks(user.username());
    }

    @GetMapping("/{id}")
    public DeckResponse getById(@PathVariable Long id) {
        return deckService.getDeck(id);
    }

    @PutMapping("/{id}")
    public DeckResponse update(@PathVariable Long id, @Valid @RequestBody CreateDeckRequest request) {
        return deckService.updateDeck(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        deckService.deleteDeck(id);
    }
}
