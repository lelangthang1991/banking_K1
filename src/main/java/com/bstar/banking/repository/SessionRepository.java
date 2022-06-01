package com.bstar.banking.repository;

import com.bstar.banking.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    @Query("FROM Session s WHERE s.refreshToken = ?1")
    Optional<Session> findSessionByRefreshToken(String refreshToken);
}
