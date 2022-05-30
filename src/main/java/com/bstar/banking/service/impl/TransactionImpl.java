package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.entity.Transaction;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.CommonResponse;
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

import static com.bstar.banking.common.TransactionString.DEPOSIT_SUCCESSFUL;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;

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


    public RestResponse<CommonResponse> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication) {


        //get email from login
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Account> account = user.getAccounts().stream()
                .filter(us -> us.getAccountNumber().equals(depositMoneyDTO.getAccountNumber()))
                .findFirst();
        Account getAccount = account.get();
        if (!getAccount.getAccountNumber().equals(depositMoneyDTO.getAccountNumber())) {
            return new RestResponse<>(new CommonResponse("400", "Account not found"));
        }
        if (!depositMoneyDTO.getPinCode().equals(getAccount.getPinCode())) {
            return new RestResponse<>(new CommonResponse("400", "Pin code not match"));
        }
        //save balance
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

        return new RestResponse<>(new CommonResponse("200", DEPOSIT_SUCCESSFUL,
                modelMapper.map(transaction, TransactionDTO.class)));


    }

}
