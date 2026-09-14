package com.java.lingvo.controller;

import com.java.lingvo.domain.dto.DeckResponse;
import com.java.lingvo.domain.dto.CreateDeckRequest;
import com.java.lingvo.security.CurrentUser;
import com.java.lingvo.service.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckControllerTest {

    @Mock
    private DeckService deckService;

    private DeckController controller() {
        return new DeckController(deckService);
    }

    private CurrentUser user(String username, boolean admin) {
        return new CurrentUser(username, admin ? List.of("USER", "ADMIN") : List.of("USER"));
    }

    @Test
    void placeOrder_delegatesUsernameAndRequestToService() {
        CreateDeckRequest request = new CreateDeckRequest();
        DeckResponse expected = new DeckResponse();
        CurrentUser user = user("alice", false);
        when(deckService.placeOrder("alice", request)).thenReturn(expected);

        assertThat(controller().placeOrder(request, user)).isSameAs(expected);
    }

    @Test
    void myOrders_delegatesUsernameToService() {
        CurrentUser user = user("alice", false);
        List<DeckResponse> expected = List.of(new DeckResponse());
        when(deckService.listMyOrders("alice")).thenReturn(expected);

        assertThat(controller().myOrders(user)).isSameAs(expected);
    }

    @Test
    void getOrder_regularUser_passesIsAdminFalse() {
        CurrentUser user = user("alice", false);
        DeckResponse expected = new DeckResponse();
        when(deckService.getOrder(1, "alice", false)).thenReturn(expected);

        assertThat(controller().getOrder(1, user)).isSameAs(expected);
    }

    @Test
    void getOrder_adminUser_passesIsAdminTrue() {
        CurrentUser user = user("admin-user", true);
        DeckResponse expected = new DeckResponse();
        when(deckService.getOrder(1, "admin-user", true)).thenReturn(expected);

        assertThat(controller().getOrder(1, user)).isSameAs(expected);
    }
}
