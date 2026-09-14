package com.java.lingvo.service;

import com.java.lingvo.domain.dto.DeckCardRequest;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.enums.OrderStatus;
import com.java.lingvo.domain.mapper.DeckMapper;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.domain.model.DeckCard;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.exception.DeckNotFoundException;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.DeckRepository;
import com.java.lingvo.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeckService {

    private static final int BULK_DISCOUNT_THRESHOLD = 5;
    private static final BigDecimal BULK_DISCOUNT_RATE = new BigDecimal("0.10");

    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;

    @Transactional
    public DeckResponse placeOrder(String customerUsername, CreateDeckRequest request) {
        Deck deck = new Deck();
        deck.setCustomerUsername(customerUsername);
        deck.setCreatedAt(OffsetDateTime.now());
        deck.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for (DeckCardRequest itemRequest : request.getItems()) {
            Card card = cardRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new CardNotFoundException(itemRequest.getProductId()));

            assertSufficientStock(card, itemRequest.getQuantity());

            BigDecimal unitPrice = card.getPrice();
            BigDecimal lineTotal = calculateLineTotal(unitPrice, itemRequest.getQuantity());
            total = total.add(lineTotal);

            DeckCard item = new DeckCard();
            item.setCard(card);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);
            deck.addItem(item);

            card.setStock(card.getStock() - itemRequest.getQuantity());
            cardRepository.save(card);
        }

        deck.setTotalPrice(total);

        deck.setStatus(OrderStatus.CONFIRMED);

        return deckMapper.toResponse(deckRepository.save(deck));
    }

    @Transactional(readOnly = true)
    public List<DeckResponse> listMyOrders(String customerUsername) {
        return deckRepository.findByCustomerUsername(customerUsername).stream()
                .map(deckMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeckResponse getOrder(Integer orderId, String requestingUsername, boolean isAdmin) {
        Deck deck = deckRepository.findById(orderId)
                .orElseThrow(() -> new DeckNotFoundException(orderId));

        if (!isAdmin && !deck.getCustomerUsername().equals(requestingUsername)) {
            throw new AccessDeniedException("You do not have permission to view this order");
        }

        return deckMapper.toResponse(deck);
    }

    private void assertSufficientStock(Card card, int requestedQuantity) {
        if (card.getStock() < requestedQuantity) {
            throw new InsufficientStockException(card.getId(), requestedQuantity, card.getStock());
        }
    }

    private BigDecimal calculateLineTotal(BigDecimal unitPrice, int quantity) {
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        if (quantity >= BULK_DISCOUNT_THRESHOLD) {
            return subtotal.subtract(subtotal.multiply(BULK_DISCOUNT_RATE));
        }
        return subtotal;
    }
}
