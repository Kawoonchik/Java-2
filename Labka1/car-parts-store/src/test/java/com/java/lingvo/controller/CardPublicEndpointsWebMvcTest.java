package com.java.lingvo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.lingvo.domain.dto.CardCreateRequest;
import com.java.lingvo.domain.dto.CardResponse;
import com.java.lingvo.exception.CardNotFoundException;
import com.java.lingvo.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CardController.class,
        excludeAutoConfiguration = {
                OAuth2ClientAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class CardPublicEndpointsWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @Test
    void getCards_returnsJsonArray() throws Exception {
        CardResponse response = new CardResponse();
        response.setId(1L);
        response.setTerm("Brake Pad");
        response.setTranslation("Гальмівна колодка");
        when(cardService.getAllCards()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].term").value("Brake Pad"))
                .andExpect(jsonPath("$[0].translation").value("Гальмівна колодка"));
    }

    @Test
    void getCardById_notFound_returns404WithErrorBody() throws Exception {
        when(cardService.getCard(404L)).thenThrow(new CardNotFoundException(404L));

        mockMvc.perform(get("/api/cards/{id}", 404))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Card not found: 404"));
    }

    @Test
    void createCard_missingRequiredFields_returns400_serviceNeverCalled() throws Exception {
        CardCreateRequest invalid = new CardCreateRequest(
                "", "", "", "");

        mockMvc.perform(post("/api/cards")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCard_validRequest_returns201() throws Exception {
        CardCreateRequest valid = new CardCreateRequest(
                "Term", "Translation", "Example", "English");
        CardResponse response = new CardResponse();
        response.setId(1L);
        when(cardService.createCard(any(CardCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isCreated());
    }
}
