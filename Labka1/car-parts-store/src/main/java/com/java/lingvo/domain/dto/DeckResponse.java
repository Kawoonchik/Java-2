package com.java.lingvo.domain.dto;

import com.java.lingvo.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeckResponse {
    private Integer id;
    private List<DeckCardResponse> items;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private OffsetDateTime createdAt;
}
