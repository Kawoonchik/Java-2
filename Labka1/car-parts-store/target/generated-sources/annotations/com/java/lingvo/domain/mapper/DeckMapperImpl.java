package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.model.Deck;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-14T17:23:00+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class DeckMapperImpl implements DeckMapper {

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

        deckResponse.setId( deck.getId() );
        deckResponse.setTitle( deck.getTitle() );
        deckResponse.setDescription( deck.getDescription() );

        return deckResponse;
    }
}
