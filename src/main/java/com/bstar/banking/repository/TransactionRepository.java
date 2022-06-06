package com.bstar.banking.repository;

import com.bstar.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE (t.transactionType = ?1 AND t.card.user.email = ?2)")
    Page<Transaction> listTransactionByType(Integer transactionType, String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.transactionType = ?1 "
            + " AND t.card.cardNumber = ?2"
            + " AND t.card.user.email = ?3)"
            + " OR  ( t.createDate BETWEEN ?4 AND ?5 )")
    Page<Transaction> listTransactionByCardAndType(Integer transactionType, String cardNumber,
                                                      String email, Date startDate, Date endDate,
                                                      Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE "
            + "(t.card.cardNumber = ?1 "
            + "AND t.card.user.email = ?2)"
            + "OR  ( t.createDate BETWEEN ?3 AND ?4 )")
    Page<Transaction> listAllTransactionByCard(String cardNumber, String email,
                                                  Date startDate, Date endDate,
                                                  Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE "
            + " (t.card.user.email = ?1) "
            + " OR ( t.createDate BETWEEN ?2 AND ?3 )")
    Page<Transaction> listAllTransaction(String email, Date startDate,
                                         Date endDate, Pageable pageable);


    @Query(value = "SELECT t FROM Transaction t WHERE"
            + " t.card.cardNumber = ?1 AND t.card.user.email = ?2"
            + " OR t.createDate BETWEEN ?3 AND ?4  ")
    Page<Transaction> getTransactionBetween(String cardNumber, String email
            , Date startDate, Date endDate
            , Pageable pageable);


}
