package com.bstar.banking.repository;

import com.bstar.banking.entity.User;
import com.bstar.banking.model.request.FilterUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> getUserByEmail(String email);

    @Query("SELECT c FROM User c WHERE c.phone = ?1")
    Optional<User> findByPhone(String phone);

    @Query("SELECT c FROM User c WHERE (:#{#userDTO.email} IS NULL OR c.email LIKE :#{#userDTO.email} || '%') " +
            " AND (:#{#userDTO.firstName} IS NULL OR c.firstName LIKE :#{#userDTO.firstName} || '%') " +
            " AND (:#{#userDTO.lastName} IS NULL OR c.lastName LIKE :#{#userDTO.lastName} || '%') " +
            " AND (:#{#userDTO.role} IS NULL OR c.role LIKE :#{#userDTO.role} || '%') ")
    Page<User> findAllUserFiltered(@Param("userDTO") FilterUserDTO userDTO, Pageable pageable);

}
