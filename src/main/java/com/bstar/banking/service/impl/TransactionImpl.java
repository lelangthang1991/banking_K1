package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.Transaction;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.ListTransactionByDatePagingRequest;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageAccount;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.TransactionRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.TransactionService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bstar.banking.common.AccountString.ACCOUNT_NUMBER_NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.TransactionString.*;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;
import static com.bstar.banking.common.UserString.PINCODE_DOES_NOT_MATCH;

@Transactional
@Service
public class TransactionImpl implements TransactionService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ModelMapper modelMapper;


    public RestResponse<?> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(depositMoneyDTO.getAccountNumber()))
                .findFirst();
        Account getAccount = account.get();
        if (!getAccount.getAccountNumber().equals(depositMoneyDTO.getAccountNumber())) {
            throw new CompareException(ACCOUNT_NUMBER_NOT_FOUND);
        }
        if (getAccount.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!depositMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        getAccount.setBalance(getAccount.getBalance() + depositMoneyDTO.getAmount());
        accountRepository.save(getAccount);
        Transaction transaction = new Transaction();
        transaction.setAmount(depositMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(1);
        transaction.setBeneficiaryAccountNumber(depositMoneyDTO.getAccountNumber());
        transaction.setBeneficiaryName(account.get().getUser().getFirstName());
        transaction.setBeneficiaryEmail(getAccount.getUser().getEmail());
        transaction.setBeneficiaryPhone(getAccount.getUser().getPhone());
        transaction.setCreatePerson(getAccount.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setBalance(getAccount.getBalance());
        transaction.setAccount(getAccount);
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(getAccount.getAccountNumber());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setBalance(getAccount.getBalance());
        return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                transactionDTO);
    }


    public RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber()))
                .findFirst();
        Account getAccount = account.get();
        if (!getAccount.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber())) {
            throw new CompareException(ACCOUNT_NUMBER_NOT_FOUND);
        }
        if (getAccount.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!withdrawMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        if (withdrawMoneyDTO.getAmount() > getAccount.getBalance()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }
        getAccount.setBalance(getAccount.getBalance() - withdrawMoneyDTO.getAmount());
        accountRepository.save(getAccount);
        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(2);
        transaction.setBeneficiaryAccountNumber(getAccount.getAccountNumber());
        transaction.setBeneficiaryName(account.get().getUser().getFirstName());
        transaction.setBeneficiaryEmail(getAccount.getUser().getEmail());
        transaction.setBeneficiaryPhone(getAccount.getUser().getPhone());
        transaction.setCreatePerson(getAccount.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setAccount(getAccount);
        transaction.setBalance(getAccount.getBalance());
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(getAccount.getAccountNumber());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setBalance(getAccount.getBalance());
        return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                transactionDTO);
    }

    public RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(transferMoneyDTO.getOwnerNumber()))
                .findFirst();
        Account accountTransfer = account.get();
        Account accountBeneficial = accountRepository.findById(transferMoneyDTO.
                        getBeneficiaryAccountNumber()).
                orElseThrow(() -> new NotFoundException(ACCOUNT_NUMBER_NOT_FOUND));
        if (accountTransfer.getAccountNumber().equals(accountBeneficial.getAccountNumber())) {
            throw new CompareException("Cannot transfer money to the same card");
        }
        if (accountBeneficial.getIsActivated().equals(false)) {
            throw new CompareException(BENEFICILAL_CARD_NOT_ACTIVATED);
        }
        if (!transferMoneyDTO.getPinCode().equals(accountTransfer.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        if (transferMoneyDTO.getAmount() > accountTransfer.getBalance()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }

        accountTransfer.setBalance(accountTransfer.getBalance() - transferMoneyDTO.getAmount());
        accountRepository.save(accountTransfer);
        accountBeneficial.setBalance(accountBeneficial.getBalance() + transferMoneyDTO.getAmount());
        accountRepository.save(accountBeneficial);
        Transaction transaction = new Transaction();
        transaction.setAmount(transferMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(3);
        transaction.setBeneficiaryAccountNumber(accountBeneficial.getAccountNumber());
        transaction.setBeneficiaryName(accountBeneficial.getUser().getFirstName() + " " + accountBeneficial.getUser().getLastName());
        transaction.setBeneficiaryEmail(accountBeneficial.getUser().getEmail());
        transaction.setBeneficiaryPhone(accountBeneficial.getUser().getPhone());
        transaction.setCreatePerson(accountTransfer.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setAccount(accountTransfer);
        transaction.setBody(transferMoneyDTO.getBody());
        transaction.setBalance(accountTransfer.getBalance());
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(accountTransfer.getAccountNumber());
        transactionDTO.setBalance(accountTransfer.getBalance());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        return new RestResponse<>(OK, TRANSFER_MONEY_SUCCESSFUL,
                transactionDTO);
    }

    @Override
    public RestResponse<ResponsePageAccount> listTransactionByAccountAndType(ListTransactionByDatePagingRequest lDate, Authentication authentication) {
        if (StringUtils.isBlank(lDate.getSortField()) || StringUtils.isBlank(lDate.getSortDir())) {
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize());
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listTransactionByAccountAndType(lDate.getTransactionType(),
                    lDate.getAccountNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getAccountNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageAccount(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        } else {
            Sort sort = Sort.by(lDate.getSortField());
            sort = lDate.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize(), sort);
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listTransactionByAccountAndType(lDate.getTransactionType(),
                    lDate.getAccountNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getAccountNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageAccount(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }

    @Override
    public RestResponse<ResponsePageAccount> listAllTransactionByAccount(ListTransactionByDatePagingRequest lDate,
                                                                         Authentication authentication) {

        if (StringUtils.isBlank(lDate.getSortField()) || StringUtils.isBlank(lDate.getSortDir())) {
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize());
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listAllTransactionByAccount(lDate.getAccountNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);

            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getAccountNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageAccount(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        } else {
            Sort sort = Sort.by(lDate.getSortField());
            sort = lDate.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize(), sort);
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listAllTransactionByAccount(lDate.getAccountNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getAccountNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageAccount(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }

}
