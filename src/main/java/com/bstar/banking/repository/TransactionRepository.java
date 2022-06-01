package com.bstar.banking.repository;

import com.bstar.banking.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE (t.transactionType=?1 AND t.account.accountNumber=?2 AND t.account.user.email=?3)")
    Page<Transaction> listTransaction(Integer transactionType, String accountNumber, String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.account.accountNumber=?1 AND t.account.user.email=?2)")
    Page<Transaction> listAllTransaction(String accountNumber, String email,Pageable pageable);

}
