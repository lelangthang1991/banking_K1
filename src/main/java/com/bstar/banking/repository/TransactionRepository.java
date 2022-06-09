package com.bstar.banking.repository;

import com.bstar.banking.entity.Transaction;
import com.bstar.banking.model.request.FilterTransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT COALESCE(SUM(t.amount),0.0) FROM Transaction t WHERE"
            + "( t.card.cardNumber = :cardNumber " +
            " AND t.card.user.email = :email " +
            " AND t.transactionType = :type" +
            " AND DATE(t.createDate) = :daily )")
    Double dailyLimit(String cardNumber, String email, Integer type, Date daily);

    @Query(value = "SELECT COALESCE(SUM(t.amount),0.0) FROM Transaction t WHERE"
            + "( t.card.cardNumber = :cardNumber " +
            " AND t.card.user.email = :email " +
            " AND t.transactionType = :type" +
            " AND MONTH(t.createDate) = :month" +
            " AND YEAR(t.createDate) = :year )")
    Double monthlyLimit(String cardNumber, String email, Integer type, Integer month, Integer year);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:#{#transDTO.cardNumber}  IS NULL OR t.card.cardNumber LIKE  :#{#transDTO.cardNumber} || '%' )" +
            " AND ( :#{#transDTO.createPerson} IS NULL OR t.createPerson LIKE :#{#transDTO.createPerson} || '%') " +
            " AND ( :#{#transDTO.startDate} IS NULL OR DATE(t.createDate) > :#{#transDTO.startDate} )" +
            " AND ( :#{#transDTO.endDate} IS NULL OR DATE(t.createDate) < :#{#transDTO.endDate} )" +
            " AND ( :#{#transDTO.transactionType} IS NULL OR t.transactionType = :#{#transDTO.transactionType}) " +
            " AND ( 1=1 OR (DATE(t.createDate) BETWEEN :#{#transDTO.startDate} AND :#{#transDTO.endDate} )) ")
    Page<Transaction> listTransaction(@Param("transDTO") FilterTransactionDTO transDTO, Pageable pageable);


    @Query("SELECT t FROM Transaction t WHERE " +
            "(:#{#transDTO.cardNumber}  IS NULL OR t.card.cardNumber LIKE  :#{#transDTO.cardNumber} || '%' )" +
            " AND ( :#{#transDTO.createPerson} IS NULL OR t.createPerson LIKE :#{#transDTO.createPerson} || '%') " +
            " AND ( :#{#transDTO.startDate} IS NULL OR DATE(t.createDate) > :#{#transDTO.startDate} )" +
            " AND ( :#{#transDTO.endDate} IS NULL OR DATE(t.createDate) < :#{#transDTO.endDate} )" +
            " AND ( :#{#transDTO.transactionType} IS NULL OR t.transactionType = :#{#transDTO.transactionType}) " +
            " AND ( 1=1 OR (DATE(t.createDate) BETWEEN :#{#transDTO.startDate} AND :#{#transDTO.endDate} )) ")
    Page<Transaction> adminListTransaction(@Param("transDTO") FilterTransactionDTO transDTO, Pageable pageable);


}
