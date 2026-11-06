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
    date = "2026-11-06T12:05:16+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (JetBrains s.r.o.)"
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
