package com.bstar.banking.repository;

import com.bstar.banking.entity.Card;
import com.bstar.banking.model.request.FilterCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    @Query("SELECT c FROM Card c WHERE (c.cardNumber LIKE '%' || ?1 || '%' OR c.cardType LIKE '%' || ?1 || '%'" +
            "OR c.createPerson LIKE '%' || ?1 || '%' OR c.updatePerson LIKE '%' || ?1 || '%')")
    Page<Card> findCardByKeyword(String keyword, Pageable pageable);

    @Query("SELECT c FROM Card c WHERE (:#{#cardDTO.cardNumber} IS NULL OR c.cardNumber LIKE :#{#cardDTO.cardNumber} || '%') " +
            " AND (:#{#cardDTO.createPerson} IS NULL OR c.createPerson LIKE :#{#cardDTO.createPerson} || '%') " +
            " AND (:#{#cardDTO.updatePerson} IS NULL OR c.updatePerson LIKE :#{#cardDTO.updatePerson} || '%')")
    Page<Card> findAllCardFiltered(@Param("cardDTO") FilterCardDTO cardDTO, Pageable pageable);

    @Query("SELECT c FROM Card c WHERE (c.cardNumber LIKE '%' || ?1 || '%' OR c.cardType LIKE '%' || ?1 || '%'" +
            "OR c.createPerson LIKE '%' || ?1 || '%' OR c.updatePerson LIKE '%' || ?1 || '%') AND c.isActivated = ?2")
    Page<Card> findCardByKeywordAndActivated(String keyword, boolean activated, Pageable pageable);

    @Query("SELECT c FROM Card c WHERE c.user.email = ?1")
    Optional<Card> findCardByEmail(String email);


}
