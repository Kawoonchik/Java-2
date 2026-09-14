package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.model.DeckCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckCardMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "lineTotal", expression = "java(orderItem.lineTotal())")
    DeckCardResponse toResponse(DeckCard deckCard);
}
