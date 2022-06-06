package com.bstar.banking.service;

import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.ListTransactionByDatePagingRequest;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    RestResponse<?> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication);

    RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication);

    RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication);

    RestResponse<ResponsePageCard> listTransactionByCardAndType(ListTransactionByDatePagingRequest listTransactionDTO, Authentication authentication);

    RestResponse<ResponsePageCard> listAllTransactionByCard(ListTransactionByDatePagingRequest listTransactionPagingRequest, Authentication authentication);

}
