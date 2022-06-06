package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.Transaction;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.ListTransactionByDatePagingRequest;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
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

import static com.bstar.banking.common.CardString.CARD_NUMBER_NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.TransactionString.*;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;
import static com.bstar.banking.common.UserString.PINCODE_DOES_NOT_MATCH;

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
        Card getCard = card.get();
        if (!getCard.getCardNumber().equals(depositMoneyDTO.getCardNumber())) {
            throw new CompareException(CARD_NUMBER_NOT_FOUND);
        }
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!depositMoneyDTO.getPinCode().equals(getCard.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        getCard.setBalance(getCard.getBalance() + depositMoneyDTO.getAmount());
        cardRepository.save(getCard);
        Transaction transaction = new Transaction();
        transaction.setAmount(depositMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(1);
        transaction.setBeneficiaryCardNumber(depositMoneyDTO.getCardNumber());
        transaction.setBeneficiaryName(card.get().getUser().getFirstName());
        transaction.setBeneficiaryEmail(getCard.getUser().getEmail());
        transaction.setBeneficiaryPhone(getCard.getUser().getPhone());
        transaction.setCreatePerson(getCard.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setBalance(getCard.getBalance());
        transaction.setCard(getCard);
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(getCard.getCardNumber());
        transactionDTO.setTransactionId(transaction.getTransactionId());
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
        Card getCard = card.get();
        if (!getCard.getCardNumber().equals(withdrawMoneyDTO.getCardNumber())) {
            throw new CompareException(CARD_NUMBER_NOT_FOUND);
        }
        if (getCard.getIsActivated().equals(false)) {
            throw new CompareException(CARD_NOT_ACTIVATED);
        }
        if (!withdrawMoneyDTO.getPinCode().equals(getCard.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        if (withdrawMoneyDTO.getAmount() > getCard.getBalance()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }
        getCard.setBalance(getCard.getBalance() - withdrawMoneyDTO.getAmount());
        cardRepository.save(getCard);
        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(2);
        transaction.setBeneficiaryCardNumber(getCard.getCardNumber());
        transaction.setBeneficiaryName(card.get().getUser().getFirstName());
        transaction.setBeneficiaryEmail(getCard.getUser().getEmail());
        transaction.setBeneficiaryPhone(getCard.getUser().getPhone());
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
        return new RestResponse<>(OK, DEPOSIT_SUCCESSFUL,
                transactionDTO);
    }

    public RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Optional<Card> card = user.getCards().stream()
                .filter(us -> us.getCardNumber().equals(transferMoneyDTO.getOwnerNumber()))
                .findFirst();
        Card cardTransfer = card.get();
        Card cardBeneficial = cardRepository.findById(transferMoneyDTO.
                        getBeneficiaryCardNumber()).
                orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        if (cardTransfer.getCardNumber().equals(cardBeneficial.getCardNumber())) {
            throw new CompareException("Cannot transfer money to the same card");
        }
        if (cardBeneficial.getIsActivated().equals(false)) {
            throw new CompareException(BENEFICIAL_CARD_NOT_ACTIVATED);
        }
        if (!transferMoneyDTO.getPinCode().equals(cardTransfer.getPinCode())) {
            throw new CompareException(PINCODE_DOES_NOT_MATCH);
        }
        if (transferMoneyDTO.getAmount() > cardTransfer.getBalance()) {
            throw new CompareException(BALANCE_IS_NOT_ENOUGH);
        }

        cardTransfer.setBalance(cardTransfer.getBalance() - transferMoneyDTO.getAmount());
        cardRepository.save(cardTransfer);
        cardBeneficial.setBalance(cardBeneficial.getBalance() + transferMoneyDTO.getAmount());
        cardRepository.save(cardBeneficial);
        Transaction transaction = new Transaction();
        transaction.setAmount(transferMoneyDTO.getAmount());
        transaction.setUnitCurrency("VND");
        transaction.setStatus(true);
        transaction.setTransactionType(3);
        transaction.setBeneficiaryCardNumber(cardBeneficial.getCardNumber());
        transaction.setBeneficiaryName(cardBeneficial.getUser().getFirstName() + " " + cardBeneficial.getUser().getLastName());
        transaction.setBeneficiaryEmail(cardBeneficial.getUser().getEmail());
        transaction.setBeneficiaryPhone(cardBeneficial.getUser().getPhone());
        transaction.setCreatePerson(cardTransfer.getUser().getEmail());
        transaction.setCreateDate(new Date());
        transaction.setCard(cardTransfer);
        transaction.setBody(transferMoneyDTO.getBody());
        transaction.setBalance(cardTransfer.getBalance());
        transactionRepository.save(transaction);
        TransactionDTO transactionDTO;
        transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOwnerNumber(cardTransfer.getCardNumber());
        transactionDTO.setBalance(cardTransfer.getBalance());
        transactionDTO.setTransactionId(transaction.getTransactionId());
        return new RestResponse<>(OK, TRANSFER_MONEY_SUCCESSFUL,
                transactionDTO);
    }

    @Override
    public RestResponse<ResponsePageCard> listTransactionByCardAndType(ListTransactionByDatePagingRequest lDate, Authentication authentication) {
        if (StringUtils.isBlank(lDate.getSortField()) || StringUtils.isBlank(lDate.getSortDir())) {
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize());
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listTransactionByCardAndType(lDate.getTransactionType(),
                    lDate.getCardNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getCardNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageCard(listPage.getNumber(),
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
            Page<Transaction> listPage = transactionRepository.listTransactionByCardAndType(lDate.getTransactionType(),
                    lDate.getCardNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getCardNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageCard(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }

    @Override
    public RestResponse<ResponsePageCard> listAllTransactionByCard(ListTransactionByDatePagingRequest lDate,
                                                                   Authentication authentication) {

        if (StringUtils.isBlank(lDate.getSortField()) || StringUtils.isBlank(lDate.getSortDir())) {
            Pageable page = PageRequest.of(lDate.getPageNumber(), lDate.getPageSize());
            String email = authentication.getName();
            User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            Page<Transaction> listPage = transactionRepository.listAllTransactionByCard(lDate.getCardNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);

            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getCardNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageCard(listPage.getNumber(),
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
            Page<Transaction> listPage = transactionRepository.listAllTransactionByCard(lDate.getCardNumber(),
                    user.getEmail(),
                    lDate.getStartDate(),
                    lDate.getEndDate(),
                    page);
            List<TransactionDTO> listDTO = listPage.getContent()
                    .parallelStream()
                    .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                    .collect(Collectors.toList());
            listDTO.forEach(l -> l.setOwnerNumber(lDate.getCardNumber()));
            return new RestResponse<>(OK, GET_LIST_SUCCESSFULLY, new ResponsePageCard(listPage.getNumber(),
                    listPage.getSize(),
                    listPage.getTotalPages(),
                    listPage.getTotalElements(),
                    listDTO));
        }
    }

}
