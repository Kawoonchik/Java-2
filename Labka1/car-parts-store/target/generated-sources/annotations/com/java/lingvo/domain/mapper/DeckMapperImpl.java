package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.DeckCardResponse;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.model.Deck;
import com.java.lingvo.domain.model.DeckCard;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-10T19:16:25+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class DeckMapperImpl implements DeckMapper {

    @Autowired
    private DeckCardMapper deckCardMapper;

    @Override
    public DeckResponse toResponse(Deck deck) {
        if ( deck == null ) {
            return null;
        }

        DeckResponse deckResponse = new DeckResponse();

        deckResponse.setId( deck.getId() );
        deckResponse.setItems( orderItemListToOrderItemResponseList( deck.getItems() ) );
        deckResponse.setTotalPrice( deck.getTotalPrice() );
        deckResponse.setStatus( deck.getStatus() );
        deckResponse.setCreatedAt( deck.getCreatedAt() );

        return deckResponse;
    }

    protected List<DeckCardResponse> orderItemListToOrderItemResponseList(List<DeckCard> list) {
        if ( list == null ) {
            return null;
        }

        List<DeckCardResponse> list1 = new ArrayList<DeckCardResponse>( list.size() );
        for ( DeckCard deckCard : list ) {
            list1.add( deckCardMapper.toResponse(deckCard) );
        }

        return list1;
    }
}
