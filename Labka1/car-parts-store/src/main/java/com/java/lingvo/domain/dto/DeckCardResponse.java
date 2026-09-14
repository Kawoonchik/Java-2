package com.java.lingvo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeckCardResponse {
    private Long id;
    private Long cardId;
    private String term;
    private String translation;
}
