package com.java.lingvo.configuration;

import com.java.lingvo.security.CurrentUserAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CurrentUserAuthenticationConverter converter;
    private final ClientRegistrationRepository clientRegistrationRepository;

    // Інжектуємо ClientRegistrationRepository для зв'язку з Keycloak при виході
    public SecurityConfig(CurrentUserAuthenticationConverter converter, ClientRegistrationRepository clientRegistrationRepository) {
        this.converter = converter;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Налаштовуємо правильний OIDC логаут
        OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/store.html");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("/store.html", true))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(converter))
                )
                // Додаємо обробник виходу
                .logout(logout -> logout
                        .logoutSuccessHandler(logoutSuccessHandler)
                );

        return http.build();
    }
}