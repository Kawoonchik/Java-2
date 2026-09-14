package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.model.Card;
import com.java.lingvo.domain.model.DeckCard;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-10T19:16:25+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class DeckCardMapperImpl implements DeckCardMapper {

    @Override
    public DeckCardResponse toResponse(DeckCard deckCard) {
        if ( deckCard == null ) {
            return null;
        }

        DeckCardResponse deckCardResponse = new DeckCardResponse();

        deckCardResponse.setProductId( orderItemProductId(deckCard) );
        deckCardResponse.setProductName( orderItemProductName(deckCard) );
        deckCardResponse.setQuantity( deckCard.getQuantity() );
        deckCardResponse.setUnitPrice( deckCard.getUnitPrice() );

        deckCardResponse.setLineTotal( deckCard.lineTotal() );

        return deckCardResponse;
    }

    private Integer orderItemProductId(DeckCard deckCard) {
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        return card.getId();
    }

    private String orderItemProductName(DeckCard deckCard) {
        Card card = deckCard.getCard();
        if ( card == null ) {
            return null;
        }
        return card.getName();
    }
}
