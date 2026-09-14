package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.model.Deck;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DeckCardMapper.class)
public interface DeckMapper {

    DeckResponse toResponse(Deck deck);
}
