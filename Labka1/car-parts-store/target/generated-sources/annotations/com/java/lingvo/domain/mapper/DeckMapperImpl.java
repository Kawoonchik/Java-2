package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CreateDeckRequest;
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
    date = "2026-10-06T12:09:42+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class DeckMapperImpl implements DeckMapper {

    @Autowired
    private DeckCardMapper deckCardMapper;

    @Override
    public Deck toEntity(CreateDeckRequest request) {
        if ( request == null ) {
            return null;
        }

        Deck deck = new Deck();

        deck.setTitle( request.getTitle() );
        deck.setDescription( request.getDescription() );

        return deck;
    }

    @Override
    public DeckResponse toResponse(Deck deck) {
        if ( deck == null ) {
            return null;
        }

        DeckResponse deckResponse = new DeckResponse();

        deckResponse.setItems( deckCardListToDeckCardResponseList( deck.getCards() ) );
        deckResponse.setId( deck.getId() );
        deckResponse.setTitle( deck.getTitle() );
        deckResponse.setDescription( deck.getDescription() );

        return deckResponse;
    }

    protected List<DeckCardResponse> deckCardListToDeckCardResponseList(List<DeckCard> list) {
        if ( list == null ) {
            return null;
        }

        List<DeckCardResponse> list1 = new ArrayList<DeckCardResponse>( list.size() );
        for ( DeckCard deckCard : list ) {
            list1.add( deckCardMapper.toResponse( deckCard ) );
        }

        return list1;
    }
}
