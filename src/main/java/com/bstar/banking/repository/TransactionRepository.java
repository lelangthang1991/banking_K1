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

    @Query("SELECT t FROM Transaction t WHERE (t.transactionType=?1 AND t.account.user.email=?2)")
    Page<Transaction> listTransactionByType(Integer transactionType, String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.transactionType=?1 AND t.account.accountNumber=?2 AND t.account.user.email=?3)")
    Page<Transaction> listTransactionByAccountAndType(Integer transactionType, String accountNumber, String email, Pageable pageable);


    @Query("SELECT t FROM Transaction t WHERE (t.account.accountNumber=?1 AND t.account.user.email=?2)")
    Page<Transaction> listAllTransactionByAccount(String accountNumber, String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.account.user.email=?1)")
    Page<Transaction> listAllTransaction(String email, Pageable pageable);

    @Query(value = "SELECT t FROM Transaction t WHERE" +
            " t.account.accountNumber=?1 AND t.account.user.email=?2" +
            "AND " +
            " t.createDate BETWEEN ?3 AND ?4")
    Page<Transaction> getTransactionBetween(String accountNumber, String email, Date startDate, Date endDate, Pageable pageable);


}
