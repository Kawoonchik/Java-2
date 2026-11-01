package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.model.Deck;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DeckCardMapper.class})
public interface DeckMapper {
    Deck toEntity(CreateDeckRequest request);
    
    @Mapping(source = "cards", target = "items")
    DeckResponse toResponse(Deck deck);
}