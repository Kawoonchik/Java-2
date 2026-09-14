package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.DeckCard;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-14T16:48:05+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class DeckCardMapperImpl implements DeckCardMapper {

    @Override
    public DeckCardResponse toResponse(DeckCard deckCard) {
        if ( deckCard == null ) {
            return null;
        }

        DeckCardResponse deckCardResponse = new DeckCardResponse();

        deckCardResponse.setCardId( deckCardCardId( deckCard ) );
        deckCardResponse.setTerm( deckCardCardTerm( deckCard ) );
        deckCardResponse.setTranslation( deckCardCardTranslation( deckCard ) );
        deckCardResponse.setId( deckCard.getId() );

        return deckCardResponse;
    }

    private Long deckCardCardId(DeckCard deckCard) {
        if ( deckCard == null ) {
            return null;
        }
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        Long id = card.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String deckCardCardTerm(DeckCard deckCard) {
        if ( deckCard == null ) {
            return null;
        }
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        String term = card.getTerm();
        if ( term == null ) {
            return null;
        }
        return term;
    }

    private String deckCardCardTranslation(DeckCard deckCard) {
        if ( deckCard == null ) {
            return null;
        }
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        String translation = card.getTranslation();
        if ( translation == null ) {
            return null;
        }
        return translation;
    }
}
