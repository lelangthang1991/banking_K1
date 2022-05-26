package com.bstar.banking.repository;

import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("SELECT c FROM Account c WHERE (c.accountNumber LIKE '%' || ?1 || '%' OR c.accountType LIKE '%' || ?1 || '%'" +
            "OR c.createPerson LIKE '%' || ?1 || '%' OR c.updatePerson LIKE '%' || ?1 || '%')")
    Page<Account> findAccountByKeyword(String keyword, Pageable pageable);

    @Query("SELECT c FROM Account c WHERE (c.accountNumber LIKE '%' || ?1 || '%' OR c.accountType LIKE '%' || ?1 || '%'" +
            "OR c.createPerson LIKE '%' || ?1 || '%' OR c.updatePerson LIKE '%' || ?1 || '%') AND c.isActivated = ?2")
    Page<Account> findAccountByKeywordAndActivated(String keyword, boolean activated, Pageable pageable);

    @Query("SELECT c FROM Account c WHERE c.user.email = ?1")
    List<Account> findAccountByEmail(String email);

}
