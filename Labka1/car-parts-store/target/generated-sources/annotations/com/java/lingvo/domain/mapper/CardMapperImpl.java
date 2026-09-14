package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.model.Card;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-10T19:16:25+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardResponse toResponse(Card card) {
        if ( card == null ) {
            return null;
        }

        CardResponse cardResponse = new CardResponse();

        cardResponse.setId( card.getId() );
        cardResponse.setSku( card.getSku() );
        cardResponse.setName( card.getName() );
        cardResponse.setCategory( card.getCategory() );
        cardResponse.setManufacturer( card.getManufacturer() );
        cardResponse.setCompatibleModel( card.getCompatibleModel() );
        cardResponse.setPrice( card.getPrice() );
        cardResponse.setStock( card.getStock() );

        return cardResponse;
    }

    @Override
    public Card toEntity(CardCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Card card = new Card();

        card.setSku( request.getSku() );
        card.setName( request.getName() );
        card.setCategory( request.getCategory() );
        card.setManufacturer( request.getManufacturer() );
        card.setCompatibleModel( request.getCompatibleModel() );
        card.setPrice( request.getPrice() );
        card.setStock( request.getStock() );

        return card;
    }

    @Override
    public void updateEntityFromRequest(CardUpdateRequest request, Card card) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            card.setName( request.getName() );
        }
        if ( request.getCategory() != null ) {
            card.setCategory( request.getCategory() );
        }
        if ( request.getManufacturer() != null ) {
            card.setManufacturer( request.getManufacturer() );
        }
        if ( request.getCompatibleModel() != null ) {
            card.setCompatibleModel( request.getCompatibleModel() );
        }
        if ( request.getPrice() != null ) {
            card.setPrice( request.getPrice() );
        }
        card.setStock( request.getStock() );
    }
}
