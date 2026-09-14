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
public class CardResponse {
    private Long id;
    private String term;
    private String translation;
    private String exampleSentence;
    private String language;
}

