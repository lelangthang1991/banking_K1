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

import static com.bstar.banking.common.AccountString.ACCOUNT_NUMBER_NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.BAD_REQUEST;
import static com.bstar.banking.common.StatusCodeString.OK;
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
            return new RestResponse<>(BAD_REQUEST, "Account not found");
        }
        if (getAccount.getIsActivated().equals(false)) {
            return new RestResponse<>(BAD_REQUEST, CARD_NOT_ACTIVATED);
        }
        if (!depositMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            return new RestResponse<>(BAD_REQUEST, PINCODE_DOES_NOT_MATCH);
        }

        try {
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
            transaction.setAccount(getAccount);
            transactionRepository.save(transaction);

            TransactionDTO transactionDTO;
            transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
            transactionDTO.setBalance(getAccount.getBalance());
            return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                    transactionDTO);

        } catch (Exception e) {
            return new RestResponse<>(BAD_REQUEST, DEPOSIT_FAIL);

        }


    }


    public RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber()))
                .findFirst();
        Account getAccount = account.get();
        if (!getAccount.getAccountNumber().equals(withdrawMoneyDTO.getAccountNumber())) {
            return new RestResponse<>(BAD_REQUEST, ACCOUNT_NUMBER_NOT_FOUND);
        }
        if (getAccount.getIsActivated().equals(false)) {
            return new RestResponse<>(BAD_REQUEST, CARD_NOT_ACTIVATED);
        }
        if (!withdrawMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            return new RestResponse<>(BAD_REQUEST, PINCODE_DOES_NOT_MATCH);
        }
        if (withdrawMoneyDTO.getAmount() > getAccount.getBalance()) {
            return new RestResponse<>(BAD_REQUEST, BALANCE_IS_NOT_ENOUGH);
        }

        try {
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

            transactionRepository.save(transaction);
            TransactionDTO transactionDTO;
            transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
            transactionDTO.setBalance(getAccount.getBalance());
            return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                    transactionDTO);
        } catch (Exception e) {
            return new RestResponse<>(BAD_REQUEST, WITHDRAW_FAIL);

        }


    }


    public RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));

        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(transferMoneyDTO.getTransferNumber()))
                .findFirst();
        Account accountTransfer = account.get();

        Account accountBeneficial = accountRepository.findById(transferMoneyDTO.
                        getBeneficiaryAccountNumber()).
                orElseThrow(() -> new NotFoundException(ACCOUNT_NUMBER_NOT_FOUND));
        if(accountTransfer.getAccountNumber().equals(accountBeneficial.getAccountNumber())){
            return new RestResponse<>(BAD_REQUEST, "Cannot transfer money to the same card");
        }
        //check accountTransfer isactivate
        if (accountBeneficial.getIsActivated().equals(false)) {
            return new RestResponse<>(BAD_REQUEST, BENEFICILAL_CARD_NOT_ACTIVATED);
        }
        if (!transferMoneyDTO.getPinCode().equals(accountTransfer.getPinCode())) {
            return new RestResponse<>(BAD_REQUEST, PINCODE_DOES_NOT_MATCH);
        }
        if (transferMoneyDTO.getAmount() > accountTransfer.getBalance()) {
            return new RestResponse<>(BAD_REQUEST, BALANCE_IS_NOT_ENOUGH);
        }


        try {
            //userTransfer
            accountTransfer.setBalance(accountTransfer.getBalance() - transferMoneyDTO.getAmount());
            accountRepository.save(accountTransfer);
            //userBeneficial
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
            transactionRepository.save(transaction);

            TransactionDTO transactionDTO;
            transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
            transactionDTO.setBalance(accountTransfer.getBalance());
            return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                    transactionDTO);
        } catch (Exception e) {
            return new RestResponse<>(BAD_REQUEST, TRANSFER_MONEY_FAIL);
        }

    }

}
