package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.mapper.DeckMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.domain.model.DeckCard;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.exception.DeckNotFoundException;
import com.java.lingvo.repository.CardRepository;
import com.java.lingvo.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckMapper deckMapper;

    @Transactional
    public DeckResponse createDeck(CreateDeckRequest request, String username) {
        Deck deck = deckMapper.toEntity(request);
        deck.setCustomerUsername(username);

        if (request.getItems() != null) {
            request.getItems().forEach(itemRequest -> {
                Card card = cardRepository.findById(itemRequest.getCardId())
                        .orElseThrow(() -> new CardNotFoundException(itemRequest.getCardId()));

                DeckCard deckCard = new DeckCard();
                deckCard.setDeck(deck);
                deckCard.setCard(card);
                deck.getCards().add(deckCard);
            });
        }

        Deck savedDeck = deckRepository.save(deck);
        return deckMapper.toResponse(savedDeck);
    }

    public List<DeckResponse> getUserDecks(String username) {
        return deckRepository.findByCustomerUsername(username).stream()
                .map(deckMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DeckResponse getDeck(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new DeckNotFoundException(id));
        return deckMapper.toResponse(deck);
    }

    public List<DeckResponse> getAllDecks() {
        return deckRepository.findAll().stream()
                .map(deckMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeckResponse updateDeck(Long id, CreateDeckRequest request) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new DeckNotFoundException(id));
        
        deck.setTitle(request.getTitle());
        deck.setDescription(request.getDescription());
        
        if (request.getItems() != null) {
            deck.getCards().clear();
            request.getItems().forEach(itemRequest -> {
                Card card = cardRepository.findById(itemRequest.getCardId())
                        .orElseThrow(() -> new CardNotFoundException(itemRequest.getCardId()));

                DeckCard deckCard = new DeckCard();
                deckCard.setDeck(deck);
                deckCard.setCard(card);
                deck.getCards().add(deckCard);
            });
        }
        
        return deckMapper.toResponse(deckRepository.save(deck));
    }

    @Transactional
    public void deleteDeck(Long id) {
        if (!deckRepository.existsById(id)) {
            throw new DeckNotFoundException(id);
        }
        deckRepository.deleteById(id);
    }
}