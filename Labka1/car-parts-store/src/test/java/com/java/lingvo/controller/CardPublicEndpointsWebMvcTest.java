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
    void getProducts_returnsJsonArray() throws Exception {
        CardResponse response = new CardResponse();
        response.setId(1);
        response.setName("Brake Pad");
        response.setPrice(new BigDecimal("45.99"));
        when(cardService.listAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Brake Pad"))
                .andExpect(jsonPath("$[0].price").value(45.99));
    }

    @Test
    void getProductById_notFound_returns404WithErrorBody() throws Exception {
        when(cardService.getById(404)).thenThrow(new CardNotFoundException(404));

        mockMvc.perform(get("/api/products/{id}", 404))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found: 404"));
    }

    @Test
    void createProduct_missingRequiredFields_returns400_serviceNeverCalled() throws Exception {
        CardCreateRequest invalid = new CardCreateRequest(
                "", "", "", "", null, null, 0);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_validRequest_returns201() throws Exception {
        CardCreateRequest valid = new CardCreateRequest(
                "SKU-1", "New Part", "ENGINE", "Bosch", null, new BigDecimal("10.00"), 5);
        CardResponse response = new CardResponse();
        response.setId(1);
        when(cardService.create(any(CardCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isCreated());
    }
}
