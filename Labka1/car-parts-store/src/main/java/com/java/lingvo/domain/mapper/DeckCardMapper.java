package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.model.DeckCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckCardMapper {
    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "card.term", target = "term")
    @Mapping(source = "card.translation", target = "translation")
    DeckCardResponse toResponse(DeckCard deckCard);
}