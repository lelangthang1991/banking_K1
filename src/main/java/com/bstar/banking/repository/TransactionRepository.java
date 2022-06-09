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

    @Query(value = "SELECT SUM(t.amount) FROM Transaction t WHERE"
            + "( t.card.cardNumber = :cardNumber " +
            " AND t.card.user.email = :email " +
            " AND t.transactionType = :type" +
            " AND DATE(t.createDate) = :daily )")
    Double dailyLimit(String cardNumber, String email, Integer type, Date daily);

    @Query(value = "SELECT SUM(t.amount) FROM Transaction t WHERE"
            + "( t.card.cardNumber = :cardNumber " +
            " AND t.card.user.email = :email " +
            " AND t.transactionType = :type" +
            " AND MONTH(t.createDate) = :month" +
            " AND YEAR(t.createDate) = :year )")
    Double monthlyLimit(String cardNumber, String email, Integer type, Integer month, Integer year);


    @Query("SELECT t FROM Transaction t WHERE (t.card.cardNumber = ?1 "
            + " OR t.createPerson = ?2"
            + " OR t.transactionType = ?3)"
            + " OR  ( t.createDate BETWEEN ?4 AND ?5 )")
    Page<Transaction> listTransaction(String cardNumber,
                                      String email,
                                      Integer transactionType,
                                      Date startDate, Date endDate,
                                      Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:#{#transDTO.cardNumber}  IS NULL OR t.card.cardNumber LIKE  :#{#transDTO.cardNumber} || '%' )" +
            " AND ( :#{#transDTO.createPerson} IS NULL OR t.createPerson LIKE :#{#transDTO.createPerson} || '%'   ) " +
            " AND ( :#{#transDTO.transactionType} IS NULL OR t.transactionType = :#{#transDTO.transactionType})" +
            " AND ( :#{#transDTO.startDate} IS NULL OR DATE(t.createDate) = :#{#transDTO.startDate}   )" +
            " AND (:#{#transDTO.endDate} IS NULL OR DATE(t.createDate) = :#{#transDTO.endDate}  ) " +
            " OR  ( DATE(t.createDate) BETWEEN :#{#transDTO.startDate} AND :#{#transDTO.endDate} ) ")
    Page<Transaction> adminListTransaction(@Param("transDTO") FilterTransactionDTO transDTO, Pageable pageable);

}
