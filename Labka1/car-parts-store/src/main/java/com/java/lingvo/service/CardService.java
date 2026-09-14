package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.mapper.CardMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional
    public CardResponse createCard(CardCreateRequest request) {
        Card card = cardMapper.toEntity(request);
        Card savedCard = cardRepository.save(card);
        return cardMapper.toResponse(savedCard);
    }

    public CardResponse getCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toResponse(card);
    }

    public List<CardResponse> getAllCards() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CardResponse updateCard(Long id, CardUpdateRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        cardMapper.updateEntityFromDto(request, card);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }
}