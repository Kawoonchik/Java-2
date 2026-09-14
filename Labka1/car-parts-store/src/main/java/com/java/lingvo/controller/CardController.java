package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public List<CardResponse> listAll() {
        return cardService.listAll();
    }

    @GetMapping("/{id}")
    public CardResponse getById(@PathVariable Integer id) {
        return cardService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(@Valid @RequestBody CardCreateRequest request) {
        return cardService.create(request);
    }

    @PutMapping("/{id}")
    public CardResponse update(@PathVariable Integer id, @Valid @RequestBody CardUpdateRequest request) {
        return cardService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        cardService.delete(id);
    }
}
