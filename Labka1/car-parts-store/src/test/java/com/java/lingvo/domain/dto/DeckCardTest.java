package com.java.lingvo.domain.dto;

import com.java.lingvo.domain.model.DeckCard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class DeckCardTest {

    @ParameterizedTest
    @ValueSource(strings = {"5.00", "12.50", "0.99", "100.00"})
    void lineTotal_valueSource(String unitPrice) {
        DeckCard item = new DeckCard();
        item.setUnitPrice(new BigDecimal(unitPrice));
        item.setQuantity(1);

        assertThat(item.lineTotal()).isEqualByComparingTo(new BigDecimal(unitPrice));
    }

    @ParameterizedTest
    @CsvSource({
            "10.00, 1, 10.00",
            "10.00, 3, 30.00",
            "9.99,  2, 19.98",
            "0.01,  100, 1.00"
    })
    void lineTotal_csvSource(String unitPrice, int quantity, String expected) {
        DeckCard item = new DeckCard();
        item.setUnitPrice(new BigDecimal(unitPrice));
        item.setQuantity(quantity);

        assertThat(item.lineTotal()).isEqualByComparingTo(new BigDecimal(expected));
    }

//    @ParameterizedTest
//    @CsvFileSource(resources = "/line-total-cases.csv", numLinesToSkip = 1)
//    void lineTotal_csvFileSource(String unitPrice, int quantity, String expected) {
//        OrderItem item = new OrderItem();
//        item.setUnitPrice(new BigDecimal(unitPrice));
//        item.setQuantity(quantity);
//
//        assertThat(item.lineTotal()).isEqualByComparingTo(new BigDecimal(expected));
//    }

    @ParameterizedTest
    @MethodSource("highQuantityLineTotals")
    void lineTotal_methodSource(BigDecimal unitPrice, int quantity, BigDecimal expected) {
        DeckCard item = new DeckCard();
        item.setUnitPrice(unitPrice);
        item.setQuantity(quantity);

        assertThat(item.lineTotal()).isEqualByComparingTo(expected);
    }

    static Stream<Arguments> highQuantityLineTotals() {
        BigDecimal unitPrice = new BigDecimal("3.33");
        return IntStream.rangeClosed(1, 5)
                .mapToObj(qty -> Arguments.of(unitPrice, qty, unitPrice.multiply(BigDecimal.valueOf(qty))));
    }
}
