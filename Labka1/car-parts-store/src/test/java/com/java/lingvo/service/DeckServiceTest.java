package com.java.lingvo.service;

import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.dto.DeckCardRequest;
import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.enums.OrderStatus;
import com.java.lingvo.domain.mapper.DeckMapper;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.exception.DeckNotFoundException;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.repository.DeckRepository;
import com.java.lingvo.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private DeckMapper deckMapper;
    
    @InjectMocks
    private DeckService deckService;

    private static final String USERNAME = "alice";

    private Card product(int id, String price, int stock) {
        Card card = new Card();
        card.setId(id);
        card.setSku("SKU-" + id);
        card.setName("Product " + id);
        card.setCategory("ENGINE");
        card.setManufacturer("Bosch");
        card.setPrice(new BigDecimal(price));
        card.setStock(stock);
        return card;
    }

    @Test
    void placeOrder_singleItem_belowBulkThreshold_noDiscountApplied() {
        Card card = product(1, "10.00", 50);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(deckMapper.toResponse(any(Deck.class))).thenAnswer(inv -> new DeckResponse());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(1, 3)));
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));
        deckService.placeOrder(USERNAME, request);

        ArgumentCaptor<Deck> orderCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(orderCaptor.capture());
        Deck saved = orderCaptor.getValue();

        assertThat(saved.getTotalPrice()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(saved.getCustomerUsername()).isEqualTo(USERNAME);
        assertThat(saved.getItems()).hasSize(1);
    }

    @ParameterizedTest
    @CsvSource({
            "4,  40.00",
            "5,  45.00",
            "10, 90.00"
    })
    void placeOrder_bulkDiscountThreshold_appliesExactlyAtFiveOrMore(int quantity, String expectedTotal) {
        Card card = product(1, "10.00", 100);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(deckMapper.toResponse(any(Deck.class))).thenAnswer(inv -> new DeckResponse());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(1, quantity)));
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));
        deckService.placeOrder(USERNAME, request);

        ArgumentCaptor<Deck> orderCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualByComparingTo(new BigDecimal(expectedTotal));
    }

    @Test
    void placeOrder_multipleItems_totalIsSumOfLineTotals() {
        Card cardA = product(1, "10.00", 50);
        Card cardB = product(2, "20.00", 50);
        when(cardRepository.findById(1)).thenReturn(Optional.of(cardA));
        when(cardRepository.findById(2)).thenReturn(Optional.of(cardB));
        when(deckMapper.toResponse(any(Deck.class))).thenAnswer(inv -> new DeckResponse());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(
                new DeckCardRequest(1, 2),
                new DeckCardRequest(2, 1)
        ));
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));
        deckService.placeOrder(USERNAME, request);

        ArgumentCaptor<Deck> orderCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getTotalPrice()).isEqualByComparingTo(new BigDecimal("40.00"));
        assertThat(orderCaptor.getValue().getItems()).hasSize(2);
    }

    @Test
    void placeOrder_decrementsStockAndSavesEachProduct() {
        Card card = product(1, "10.00", 50);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(deckMapper.toResponse(any(Deck.class))).thenAnswer(inv -> new DeckResponse());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(1, 7)));
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));
        deckService.placeOrder(USERNAME, request);

        assertThat(card.getStock()).isEqualTo(43);
        verify(cardRepository).save(card);
    }

    @Test
    void placeOrder_snapshotsUnitPriceOnItem_independentOfLaterProductPriceChanges() {
        Card card = product(1, "15.50", 20);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        when(deckMapper.toResponse(any(Deck.class))).thenAnswer(inv -> new DeckResponse());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(1, 1)));
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));
        deckService.placeOrder(USERNAME, request);

        ArgumentCaptor<Deck> orderCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(orderCaptor.capture());

        assertThat(orderCaptor.getValue().getItems().getFirst().getUnitPrice())
                .isEqualByComparingTo(new BigDecimal("15.50"));
    }

    @Test
    void placeOrder_productDoesNotExist_throwsProductNotFoundException() {
        when(cardRepository.findById(99)).thenReturn(Optional.empty());

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(99, 1)));

        assertThatThrownBy(() -> deckService.placeOrder(USERNAME, request))
                .isInstanceOf(CardNotFoundException.class);

        verifyNoInteractions(deckRepository);
    }

    @Test
    void placeOrder_insufficientStock_throwsInsufficientStockException() {
        Card card = product(1, "10.00", 2);
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));

        CreateDeckRequest request = new CreateDeckRequest();
        request.setItems(List.of(new DeckCardRequest(1, 5)));

        assertThatThrownBy(() -> deckService.placeOrder(USERNAME, request))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("requested 5")
                .hasMessageContaining("available 2");

        verify(deckRepository, never()).save(any());
    }

    @Test
    void listMyOrders_delegatesToRepositoryAndMapsResults() {
        Deck deck = new Deck();
        DeckResponse response = new DeckResponse();
        when(deckRepository.findByCustomerUsername(USERNAME)).thenReturn(List.of(deck));
        when(deckMapper.toResponse(deck)).thenReturn(response);

        List<DeckResponse> result = deckService.listMyOrders(USERNAME);

        assertThat(result).containsExactly(response);
    }

    @Test
    void listMyOrders_noOrders_returnsEmptyList() {
        when(deckRepository.findByCustomerUsername(USERNAME)).thenReturn(List.of());

        assertThat(deckService.listMyOrders(USERNAME)).isEmpty();
    }

    @Test
    void getOrder_ownerRequestingOwnOrder_succeeds() {
        Deck deck = new Deck();
        deck.setCustomerUsername(USERNAME);
        DeckResponse response = new DeckResponse();
        when(deckRepository.findById(1)).thenReturn(Optional.of(deck));
        when(deckMapper.toResponse(deck)).thenReturn(response);

        DeckResponse result = deckService.getOrder(1, USERNAME, false);

        assertThat(result).isSameAs(response);
    }

    @Test
    void getOrder_adminRequestingSomeoneElsesOrder_succeeds() {
        Deck deck = new Deck();
        deck.setCustomerUsername("someone-else");
        DeckResponse response = new DeckResponse();
        when(deckRepository.findById(1)).thenReturn(Optional.of(deck));
        when(deckMapper.toResponse(deck)).thenReturn(response);

        DeckResponse result = deckService.getOrder(1, "admin-user", true);

        assertThat(result).isSameAs(response);
    }

    @Test
    void getOrder_nonOwnerNonAdmin_throwsAccessDenied() {
        Deck deck = new Deck();
        deck.setCustomerUsername("someone-else");
        when(deckRepository.findById(1)).thenReturn(Optional.of(deck));

        assertThatThrownBy(() -> deckService.getOrder(1, USERNAME, false))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void getOrder_orderDoesNotExist_throwsOrderNotFoundException() {
        when(deckRepository.findById(404)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deckService.getOrder(404, USERNAME, false))
                .isInstanceOf(DeckNotFoundException.class);
    }
}
