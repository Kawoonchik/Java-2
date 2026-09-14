package com.java.lingvo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeckResponse {
    private Long id;
    private String title;
    private String description;
    private List<DeckCardResponse> items;
}
