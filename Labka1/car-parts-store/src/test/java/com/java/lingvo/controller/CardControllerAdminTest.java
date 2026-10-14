package com.java.lingvo.controller;

import com.java.lingvo.configuration.SecurityConfig;
import com.java.lingvo.security.CurrentUser;
import com.java.lingvo.security.CurrentUserAuthenticationToken;
import com.java.lingvo.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import com.java.lingvo.security.CurrentUserAuthenticationConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardController.class)
class CardControllerAdminTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private CurrentUserAuthenticationConverter converter;

    @MockitoBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void deleteCard_WithAdminRole_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long cardId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/cards/{id}", cardId)
                        .with(authenticatedUser("ADMIN")))
                .andExpect(status().isNoContent());

        verify(cardService).deleteCard(cardId);
    }

    @Test
    void deleteCard_WithUserRole_ShouldReturnForbidden() throws Exception {
        // Arrange
        Long cardId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/cards/{id}", cardId)
                        .with(authenticatedUser("USER")))
                .andExpect(status().isForbidden());
    }

    private RequestPostProcessor authenticatedUser(String role) {
        return request -> {
            Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"), Map.of("preferred_username", "testuser"));
            
            CurrentUser currentUser = new CurrentUser("testuser", List.of(role));
            CurrentUserAuthenticationToken authentication = new CurrentUserAuthenticationToken(
                    jwt, currentUser, List.of(
                            new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                    ));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Set the security context into the session if necessary
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            
            return request;
        };
    }
}
