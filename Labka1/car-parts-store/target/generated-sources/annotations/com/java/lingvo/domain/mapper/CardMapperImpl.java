package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.model.Card;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-14T17:23:00+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public Card toEntity(CardCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Card card = new Card();

        card.setTerm( request.getTerm() );
        card.setTranslation( request.getTranslation() );
        card.setExampleSentence( request.getExampleSentence() );
        card.setLanguage( request.getLanguage() );

        return card;
    }

    @Override
    public void updateEntityFromDto(CardUpdateRequest request, Card card) {
        if ( request == null ) {
            return;
        }

        card.setTerm( request.getTerm() );
        card.setTranslation( request.getTranslation() );
        card.setExampleSentence( request.getExampleSentence() );
        card.setLanguage( request.getLanguage() );
    }

    @Override
    public CardResponse toResponse(Card card) {
        if ( card == null ) {
            return null;
        }

        CardResponse cardResponse = new CardResponse();

        cardResponse.setId( card.getId() );
        cardResponse.setTerm( card.getTerm() );
        cardResponse.setTranslation( card.getTranslation() );
        cardResponse.setExampleSentence( card.getExampleSentence() );
        cardResponse.setLanguage( card.getLanguage() );

        return cardResponse;
    }
}
