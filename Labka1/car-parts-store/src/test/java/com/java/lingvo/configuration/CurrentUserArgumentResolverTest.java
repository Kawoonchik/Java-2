package com.java.lingvo.configuration;

import com.java.lingvo.annotation.InjectCurrentUser;
import com.java.lingvo.security.CurrentUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserArgumentResolverTest {

    private CurrentUserArgumentResolver resolver;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private ModelAndViewContainer mavContainer;

    @Mock
    private NativeWebRequest webRequest;

    @Mock
    private WebDataBinderFactory binderFactory;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        resolver = new CurrentUserArgumentResolver();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void supportsParameter_ShouldReturnTrue_WhenAnnotatedWithInjectCurrentUser() {
        // Arrange
        when(methodParameter.hasParameterAnnotation(InjectCurrentUser.class)).thenReturn(true);

        // Act
        boolean result = resolver.supportsParameter(methodParameter);

        // Assert
        assertTrue(result, "Should support parameter annotated with @InjectCurrentUser");
    }

    @Test
    void supportsParameter_ShouldReturnFalse_WhenNotAnnotatedWithInjectCurrentUser() {
        // Arrange
        when(methodParameter.hasParameterAnnotation(InjectCurrentUser.class)).thenReturn(false);

        // Act
        boolean result = resolver.supportsParameter(methodParameter);

        // Assert
        assertFalse(result, "Should not support parameter without @InjectCurrentUser");
    }

    @Test
    void resolveArgument_ShouldReturnCurrentUser_WhenAuthenticationIsValid() {
        // Arrange
        CurrentUser expectedUser = new CurrentUser("testuser", List.of("USER"));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(expectedUser);

        // Act
        Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // Assert
        assertEquals(expectedUser, result, "Should return the CurrentUser from security context");
    }

    @Test
    void resolveArgument_ShouldReturnNull_WhenAuthenticationIsMissing() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        Object result = resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        // Assert
        assertNull(result, "Should return null when authentication is missing");
    }
}
