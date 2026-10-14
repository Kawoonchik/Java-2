package com.java.lingvo.domain.mapper;

import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.domain.dto.CardUpdateRequest;
import com.java.lingvo.domain.model.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CardMapperImpl.class)
class CardMapperImplTest {

    @Autowired
    private CardMapper mapper;
    @Test
    void toResponse_mapsAllFields() {
        Card card = new Card();
        card.setId(1L);
        card.setTerm("Hola");
        card.setTranslation("Hello");
        card.setExampleSentence("Hola, ¿cómo estás?");
        card.setLanguage("Spanish");

        CardResponse response = mapper.toResponse(card);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTerm()).isEqualTo("Hola");
        assertThat(response.getTranslation()).isEqualTo("Hello");
        assertThat(response.getExampleSentence()).isEqualTo("Hola, ¿cómo estás?");
        assertThat(response.getLanguage()).isEqualTo("Spanish");
    }

    @Test
    void toEntity_mapsCreateRequestFields() {
        CardCreateRequest request = new CardCreateRequest("Konnichiwa", "Hello", "Konnichiwa, genki?", "Japanese");

        Card entity = mapper.toEntity(request);

        assertThat(entity.getTerm()).isEqualTo("Konnichiwa");
        assertThat(entity.getTranslation()).isEqualTo("Hello");
        assertThat(entity.getExampleSentence()).isEqualTo("Konnichiwa, genki?");
        assertThat(entity.getLanguage()).isEqualTo("Japanese");
        assertThat(entity.getId()).isNull();
    }

    @Test
    void updateEntityFromRequest_overwritesMutableFields_leavesIdAlone() {
        Card existing = new Card();
        existing.setId(7L);
        existing.setTerm("Old Term");
        existing.setTranslation("Old Trans");
        existing.setLanguage("Old Lang");

        CardUpdateRequest request = new CardUpdateRequest("New Term", "New Trans", "New Example", "New Lang");

        mapper.updateEntityFromDto(request, existing);

        assertThat(existing.getId()).isEqualTo(7L);
        assertThat(existing.getTerm()).isEqualTo("New Term");
        assertThat(existing.getTranslation()).isEqualTo("New Trans");
        assertThat(existing.getExampleSentence()).isEqualTo("New Example");
        assertThat(existing.getLanguage()).isEqualTo("New Lang");
    }
}
