package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.model.Card;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toEntity(CardCreateRequest request);
    void updateEntityFromDto(CardUpdateRequest request, @MappingTarget Card card);
    CardResponse toResponse(Card card);
}
