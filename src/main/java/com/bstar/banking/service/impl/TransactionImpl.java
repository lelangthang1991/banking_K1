package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.Transaction;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.AccountRepository;
import com.bstar.banking.repository.TransactionRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.bstar.banking.common.TransactionString.*;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;
import static com.bstar.banking.common.UserString.PINCODE_DOES_NOT_MATCH;

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
            return new RestResponse<>("400", "Account not found");
        }
        if (!depositMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            return new RestResponse<>("400", "Pin code not match");
        }
        getAccount.setBalance(getAccount.getBalance() + depositMoneyDTO.getAmount());
        accountRepository.save(getAccount);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
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
        transaction.setAccount(getAccount);
        transactionRepository.save(transaction);
        return new RestResponse<>("200", DEPOSIT_SUCCESSFUL,
                modelMapper.map(transaction, TransactionDTO.class));
    }


    public RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber()))
                .findFirst();
        Account getAccount = account.get();
        if (!getAccount.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber())) {
            return new RestResponse<>("400", "Account not found");
        }
        if (!withdrawMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            return new RestResponse<>("400", "Pin code not match");
        }
        if (withdrawMoneyDTO.getAmount() > getAccount.getBalance()) {
            return new RestResponse<>("400", "Balance is not enough");
        }

        getAccount.setBalance(getAccount.getBalance() - withdrawMoneyDTO.getAmount());
        accountRepository.save(getAccount);
        Transaction transaction = new Transaction();
        transaction.setTransactionId(2);
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
        transactionRepository.save(transaction);
        return new RestResponse<>("200", WITHDRAW_SUCCESSFUL);


    }


    public RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));

        //bank1
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(transferMoneyDTO.getTransferNumber()))
                .findFirst();
        Account accountTransfer = account.get();
        // bank2
        User userBeneficial = userRepository.findById(transferMoneyDTO.getBeneficiaryEmail())
                .orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));


        Optional<Account> account2 = userBeneficial.getAccounts().stream().filter(u -> u.getAccountNumber()
                .equals(transferMoneyDTO.getBeneficiaryAccountNumber())).findFirst();
        Account accountBeneficial = account2.get();


        if (!transferMoneyDTO.getPinCode().equals(accountTransfer.getPinCode())) {
            return new RestResponse<>("400", PINCODE_DOES_NOT_MATCH);
        }
        if (transferMoneyDTO.getAmount() > accountTransfer.getBalance()) {
            return new RestResponse<>("400", "Balance is not enough");
        }

        try {
            //userTransfer
            accountTransfer.setBalance(accountTransfer.getBalance() - transferMoneyDTO.getAmount());
            accountRepository.save(accountTransfer);


            //userBeneficial
            accountBeneficial.setBalance(accountBeneficial.getBalance() + transferMoneyDTO.getAmount());
            accountRepository.save(accountBeneficial);


            Transaction transaction = new Transaction();
            transaction.setTransactionId(3);
            transaction.setAmount(transferMoneyDTO.getAmount());
            transaction.setUnitCurrency("VND");
            transaction.setStatus(true);
            transaction.setTransactionType(3);
            transaction.setBeneficiaryAccountNumber(accountBeneficial.getAccountNumber());
            transaction.setBeneficiaryName(accountBeneficial.getUser().getFirstName());
            transaction.setBeneficiaryEmail(accountBeneficial.getUser().getEmail());
            transaction.setBeneficiaryPhone(accountBeneficial.getUser().getPhone());
            transaction.setCreatePerson(accountTransfer.getUser().getEmail());
            transaction.setCreateDate(new Date());
            transaction.setAccount(accountTransfer);
            transactionRepository.save(transaction);


            return new RestResponse<>("200", TRANSFER_MONEY_SUCCESSFUL);

        } catch (Exception e){
            return new RestResponse<>("400", TRANSFER_MONEY_FAIL);
        }

    }

}
