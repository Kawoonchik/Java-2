package com.java.lingvo.controller;

import com.java.lingvo.annotation.AdminDeleteMapping;
import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.java.lingvo.annotation.InjectCurrentUser;
import com.java.lingvo.security.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public List<CardResponse> listAll() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public CardResponse getById(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(@Valid @RequestBody CardCreateRequest request) {
        return cardService.createCard(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse update(@PathVariable Long id, @Valid @RequestBody CardUpdateRequest request) {
        return cardService.updateCard(id, request);
    }

    @AdminDeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cardService.deleteCard(id);
    }

    @GetMapping("/my-profile")
    public String getMyProfile(@InjectCurrentUser CurrentUser user) {
        return "Привіт! Твій внутрішній ID: " + user.username();
    }
}
