package com.bstar.banking.repository;

import com.bstar.banking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT c FROM User c WHERE (c.email LIKE ?1 OR c.firstName LIKE ?1" +
            "OR c.lastName LIKE ?1 OR c.phone LIKE ?1 OR c.address LIKE ?1)")
    Page<User> findCustomerByKeyword(String keyword, Pageable pageable);

    @Query("SELECT c FROM User c WHERE (c.email LIKE ?1 OR c.firstName LIKE ?1" +
            "OR c.lastName LIKE ?1 OR c.phone LIKE ?1 OR c.address LIKE ?1) AND c.isActivated = ?2")
    Page<User> findCustomerByKeywordAndActivated(String keyword, boolean activated, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> getUserByEmail(String email);
}
