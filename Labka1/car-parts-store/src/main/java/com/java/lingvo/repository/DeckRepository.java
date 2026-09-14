package com.java.lingvo.repository;

import com.java.lingvo.domain.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("SELECT DISTINCT d FROM Deck d " +
            "JOIN FETCH d.cards dc " +
            "JOIN FETCH dc.card " +
            "WHERE d.customerUsername = :username")
    List<Deck> findByCustomerUsername(@Param("username") String username);
}