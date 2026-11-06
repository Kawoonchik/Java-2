package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.DeckCard;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-06T12:05:16+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (JetBrains s.r.o.)"
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
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        return card.getId();
    }

    private String deckCardCardTerm(DeckCard deckCard) {
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        return card.getTerm();
    }

    private String deckCardCardTranslation(DeckCard deckCard) {
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        return card.getTranslation();
    }
}
