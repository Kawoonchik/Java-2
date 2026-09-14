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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckResponse placeOrder(@Valid @RequestBody CreateDeckRequest request,
                                   @AuthenticationPrincipal CurrentUser user) {
        return deckService.placeOrder(user.username(), request);
    }

    @GetMapping("/mine")
    public List<DeckResponse> myOrders(@AuthenticationPrincipal CurrentUser user) {
        return deckService.listMyOrders(user.username());
    }

    @GetMapping("/{id}")
    public DeckResponse getOrder(@PathVariable Integer id, @AuthenticationPrincipal CurrentUser user) {
        return deckService.getOrder(id, user.username(), user.hasRole("ADMIN"));
    }
}
