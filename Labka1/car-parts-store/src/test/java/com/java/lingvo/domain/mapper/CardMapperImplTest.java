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
        card.setId(1);
        card.setSku("BRK-PAD-001");
        card.setName("Brake Pad");
        card.setCategory("BRAKES");
        card.setManufacturer("Bosch");
        card.setCompatibleModel("Corolla");
        card.setPrice(new BigDecimal("45.99"));
        card.setStock(10);

        CardResponse response = mapper.toResponse(card);

        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getSku()).isEqualTo("BRK-PAD-001");
        assertThat(response.getName()).isEqualTo("Brake Pad");
        assertThat(response.getCategory()).isEqualTo("BRAKES");
        assertThat(response.getManufacturer()).isEqualTo("Bosch");
        assertThat(response.getCompatibleModel()).isEqualTo("Corolla");
        assertThat(response.getPrice()).isEqualByComparingTo("45.99");
        assertThat(response.getStock()).isEqualTo(10);
    }

    @Test
    void toEntity_mapsCreateRequestFields() {
        CardCreateRequest request = new CardCreateRequest(
                "OIL-FLT-014", "Oil Filter", "ENGINE", "Mann-Filter",
                "Golf", new BigDecimal("9.50"), 300);

        Card entity = mapper.toEntity(request);

        assertThat(entity.getSku()).isEqualTo("OIL-FLT-014");
        assertThat(entity.getName()).isEqualTo("Oil Filter");
        assertThat(entity.getPrice()).isEqualByComparingTo("9.50");
        assertThat(entity.getStock()).isEqualTo(300);
        assertThat(entity.getId()).isNull();
    }

    @Test
    void updateEntityFromRequest_overwritesMutableFields_leavesIdAndSkuAlone() {
        Card existing = new Card();
        existing.setId(7);
        existing.setSku("SPK-PLG-007");
        existing.setName("Old Name");
        existing.setCategory("OLD");
        existing.setManufacturer("Old Manu");
        existing.setPrice(new BigDecimal("1.00"));
        existing.setStock(1);

        CardUpdateRequest request = new CardUpdateRequest(
                "New Name", "NEW", "New Manu", "New Model", new BigDecimal("99.99"), 50);

        mapper.updateEntityFromRequest(request, existing);

        assertThat(existing.getId()).isEqualTo(7);
        assertThat(existing.getSku()).isEqualTo("SPK-PLG-007");
        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getCategory()).isEqualTo("NEW");
        assertThat(existing.getPrice()).isEqualByComparingTo("99.99");
        assertThat(existing.getStock()).isEqualTo(50);
    }
}
