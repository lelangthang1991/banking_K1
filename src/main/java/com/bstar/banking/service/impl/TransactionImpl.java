package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.Transaction;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.FilterTransactionDTO;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.model.response.TransactionResponse;
import com.bstar.banking.repository.CardRepository;
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

import static com.bstar.banking.common.CardString.*;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.TransactionString.*;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;
import static com.bstar.banking.common.UserString.PIN_CODE_DOES_NOT_MATCH;

@Transactional
@Service
public class TransactionImpl implements TransactionService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ModelMapper modelMapper;


    public RestResponse<?> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Card> card = user.getCards().stream()
                .filter(us -> us.getCardNumber().equals(depositMoneyDTO.getCardNumber()))
                .findFirst();
        Card getCard = card.orElseThrow(() -> new NotFoundException(CARD_NOT_FOUND));
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (depositMoneyDTO.getAmount() < 50000) {
            throw new CompareException(DEPOSIT_AMOUNT_NOT_ENOUGH);
        }
        if (!getCard.getCardNumber().equals(depositMoneyDTO.getCardNumber())) {
            throw new CompareException(CARD_NUMBER_NOT_FOUND);
        }
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!depositMoneyDTO.getPinCode().equals(getCard.getPinCode())) {
            throw new CompareException(PIN_CODE_DOES_NOT_MATCH);
        }
        getCard.setBalance(getCard.getBalance() + depositMoneyDTO.getAmount());
        cardRepository.save(getCard);
        Transaction transaction = new Transaction();
        transaction.setAmount(depositMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(1);
        transaction.setBeneficiaryCardNumber(depositMoneyDTO.getCardNumber());
        transaction.setBeneficiaryName(card.get().getUser().getFirstName() + " " + card.get().getUser().getLastName());
        transaction.setBeneficiaryEmail(getCard.getUser().getEmail());
        transaction.setBeneficiaryPhone(getCard.getUser().getPhone());
        transaction.setBody(card.get().getUser().getFirstName() + " " + card.get().getUser().getLastName() + " " + "Deposit money");
        transaction.setCreatePerson(getCard.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setBalance(getCard.getBalance());
        transaction.setFee((double) 0);
        transaction.setCard(getCard);
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(getCard.getCardNumber());
        transactionDTO.setBalance(getCard.getBalance());
        return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                transactionDTO);
    }


    public RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Card> card = user.getCards().stream()
                .filter(us -> us.getCardNumber().equals(withdrawMoneyDTO.getCardNumber()))
                .findFirst();
        Card getCard = card.orElseThrow(() -> new NotFoundException(CARD_NOT_FOUND));
        Double fee = 0.0;
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (getCard.getLevel().equals(1)) {
            fee = 1000.0;
        }
        if (!getCard.getCardNumber().equals(withdrawMoneyDTO.getCardNumber())) {
            throw new CompareException(CARD_NUMBER_NOT_FOUND);
        }
        if (withdrawMoneyDTO.getAmount() < 50000) {
            throw new CompareException(WITHDRAW_AMOUNT_NOT_ENOUGH);
        }
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!withdrawMoneyDTO.getPinCode().equals(getCard.getPinCode())) {
            throw new CompareException(PIN_CODE_DOES_NOT_MATCH);
        }
        if (withdrawMoneyDTO.getAmount() + fee > getCard.getBalance()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }
        Transaction transaction = new Transaction();
        transaction.setFee(fee);
        getCard.setBalance(getCard.getBalance() - withdrawMoneyDTO.getAmount() - fee);
        cardRepository.save(getCard);
        transaction.setAmount(withdrawMoneyDTO.getAmount() + fee);
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(2);
        transaction.setBeneficiaryCardNumber(getCard.getCardNumber());
        transaction.setBeneficiaryName(card.get().getUser().getFirstName());
        transaction.setBeneficiaryEmail(getCard.getUser().getEmail());
        transaction.setBeneficiaryPhone(getCard.getUser().getPhone());
        transaction.setBody(getCard.getUser().getFirstName() + " " + getCard.getUser().getLastName() + " " + "Withdraw money");
        transaction.setCreatePerson(getCard.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setCard(getCard);
        transaction.setBalance(getCard.getBalance());
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(getCard.getCardNumber());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        transactionDTO.setBalance(getCard.getBalance());
        return new RestResponse<>(OK, WITHDRAW_SUCCESSFUL,
                transactionDTO);
    }

    public RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Card> card = user.getCards().stream()
                .filter(us -> us.getCardNumber().equals(transferMoneyDTO.getOwnerNumber()))
                .findFirst();
        Card cardTransfer = card.orElseThrow(() -> new NotFoundException(CARD_NOT_FOUND));
        Card cardBeneficial = cardRepository.findById(transferMoneyDTO.
                        getBeneficiaryCardNumber()).
                orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));

        Date currentDate = new Date();
        Double monthlyLimit = transactionRepository.monthlyLimit(cardTransfer.getCardNumber(),
                email, 3, currentDate.getMonth() + 1, currentDate.getYear() + 1900);

        Double fee = 0.0;
        if (cardTransfer.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (cardTransfer.getLevel().equals(1)) {
            fee = 1000.0;
        }
        if (transferMoneyDTO.getAmount() < 50000) {
            throw new CompareException(TRANSFER_AMOUNT_NOT_ENOUGH);
        }
        if (cardTransfer.getCardNumber().equals(cardBeneficial.getCardNumber())) {
            throw new CompareException(CAN_NOT_TRANSFER_MONEY_TO_THE_SAME_CARD);
        }
        if (cardBeneficial.getIsActivated().equals(false)) {
            throw new CompareException(BENEFICIAL_CARD_NOT_ACTIVATED);
        }
        if (!transferMoneyDTO.getPinCode().equals(cardTransfer.getPinCode())) {
            throw new CompareException(PIN_CODE_DOES_NOT_MATCH);
        }

        if (transferMoneyDTO.getAmount() + fee > cardTransfer.getBalance() && transferMoneyDTO.getAmount() + fee > cardTransfer.getDailyAvailableTransfer()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }
        if (monthlyLimit > cardTransfer.getMonthlyLimitAmount()) {
            throw new CompareException(THE_MONTHLY_TRANSFER_EXCEEDED);
        } else if ((transferMoneyDTO.getAmount() + fee) > (cardTransfer.getMonthlyLimitAmount() - monthlyLimit)) {
            throw new CompareException(THE_MONTHLY_TRANSFER_EXCEEDED);
        } else if ((transferMoneyDTO.getAmount() + fee) > cardTransfer.getDailyAvailableTransfer()) {
            throw new CompareException(THE_DAILY_TRANSFER_EXCEEDED_TRY_AGAIN_THE_NEXT_DAY);
        }

        //transfer transaction
        cardTransfer.setBalance(cardTransfer.getBalance() - transferMoneyDTO.getAmount() - fee);
        cardRepository.save(cardTransfer);
        Transaction transactionTransfer = new Transaction();
        transactionTransfer.setAmount(transferMoneyDTO.getAmount() + fee);
        transactionTransfer.setUnitCurrency("VND");
        transactionTransfer.setStatus(true);
        transactionTransfer.setTransactionType(3);
        transactionTransfer.setBeneficiaryCardNumber(cardBeneficial.getCardNumber());
        transactionTransfer.setBeneficiaryName(cardBeneficial.getUser().getFirstName() + " " + cardBeneficial.getUser().getLastName());
        transactionTransfer.setBeneficiaryEmail(cardBeneficial.getUser().getEmail());
        transactionTransfer.setBeneficiaryPhone(cardBeneficial.getUser().getPhone());
        transactionTransfer.setCreatePerson(cardTransfer.getUser().getEmail());
        transactionTransfer.setCreateDate(new Date());
        transactionTransfer.setFee(fee);
        transactionTransfer.setCard(cardTransfer);
        transactionTransfer.setBody(transferMoneyDTO.getBody());
        transactionTransfer.setBalance(cardTransfer.getBalance());
        transactionRepository.save(transactionTransfer);

        cardTransfer.setMonthlyAvailableTransfer(cardTransfer.getMonthlyAvailableTransfer() - (transferMoneyDTO.getAmount() + fee));
        cardTransfer.setDailyAvailableTransfer(cardTransfer.getDailyAvailableTransfer() - (transferMoneyDTO.getAmount() + fee));
        cardRepository.save(cardTransfer);
        //receive Transaction
        cardBeneficial.setBalance(cardBeneficial.getBalance() + transferMoneyDTO.getAmount());
        cardRepository.save(cardBeneficial);
        Transaction transactionReceive = new Transaction();
        transactionReceive.setAmount(transferMoneyDTO.getAmount());
        transactionReceive.setBalance(cardBeneficial.getBalance());
        transactionReceive.setUnitCurrency("VND");
        transactionReceive.setStatus(true);
        transactionReceive.setTransactionType(4);
        transactionReceive.setBody(transferMoneyDTO.getBody());
        transactionReceive.setTransferNumber(cardTransfer.getCardNumber());
        transactionReceive.setBeneficiaryCardNumber(cardBeneficial.getCardNumber());
        transactionReceive.setBeneficiaryName(cardBeneficial.getUser().getFirstName() + " " + cardBeneficial.getUser().getLastName());
        transactionReceive.setBeneficiaryEmail(cardBeneficial.getUser().getEmail());
        transactionReceive.setBeneficiaryPhone(cardBeneficial.getUser().getPhone());
        transactionReceive.setCreatePerson(cardTransfer.getUser().getEmail());
        transactionReceive.setCreateDate(new Date());
        transactionReceive.setFee(0.0);
        transactionReceive.setCard(cardBeneficial);
        transactionReceive.setBody(transferMoneyDTO.getBody());
        transactionReceive.setBalance(cardTransfer.getBalance());
        transactionRepository.save(transactionReceive);

        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transactionTransfer, TransactionDTO.class);
        transactionDTO.setOwnerNumber(cardTransfer.getCardNumber());
        transactionDTO.setBalance(cardTransfer.getBalance());
        transactionDTO.setTransactionId(transactionTransfer.getTransactionId());
        transactionDTO.setFee(transactionTransfer.getFee());
        return new RestResponse<>(OK, TRANSFER_MONEY_SUCCESSFUL,
                transactionDTO);
    }

    @Override
    public RestResponse<ResponsePageCard> listTransaction(FilterTransactionDTO lDate,
                                                          Authentication authentication) {

        if (StringUtils.isBlank(lDate.getSortField()) || StringUtils.isBlank(lDate.getSortDir())) {
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize());
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listTransaction(lDate, page);
            List<TransactionResponse> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionResponse.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_SUCCESSFUL, new ResponsePageCard(listPage.getNumber(),
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
            Page<Transaction> listPage = transactionRepository.listTransaction(lDate, page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_SUCCESSFUL, new ResponsePageCard(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }

    @Override
    public RestResponse<ResponsePageCard> listAdminTransaction(FilterTransactionDTO transaction) {
        if (StringUtils.isBlank(transaction.getSortField()) || StringUtils.isBlank(transaction.getSortDir())) {
            Pageable page = PageRequest.of(transaction.getPageNumber(), transaction.getPageSize());
            Page<Transaction> listPage = transactionRepository.adminListTransaction(transaction, page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(tran -> modelMapper.map(tran, TransactionDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_SUCCESSFUL, new ResponsePageCard(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        } else {
            Sort sort = Sort.by(transaction.getSortField());
            sort = transaction.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable page = PageRequest.of(transaction.getPageNumber(), transaction.getPageSize(), sort);
            Page<Transaction> listPage = transactionRepository.adminListTransaction(transaction, page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(tran -> modelMapper.map(tran, TransactionDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_SUCCESSFUL, new ResponsePageCard(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }


}
