package com.java.lingvo.service;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.mapper.CardMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional(readOnly = true)
    public List<CardResponse> listAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CardResponse getById(Integer id) {
        return cardMapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public CardResponse create(CardCreateRequest request) {
        Card card = cardMapper.toEntity(request);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public CardResponse update(Integer id, CardUpdateRequest request) {
        Card card = findOrThrow(id);
        cardMapper.updateEntityFromRequest(request, card);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    @Transactional
    public void delete(Integer id) {
        if (!cardRepository.existsById(id)) {
            throw new CardNotFoundException(id);
        }
        cardRepository.deleteById(id);
    }

    private Card findOrThrow(Integer id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
    }
}
