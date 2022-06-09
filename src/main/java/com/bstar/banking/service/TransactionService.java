package com.bstar.banking.service;

import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.FilterTransactionDTO;
import com.bstar.banking.model.request.TransactionDTO;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    RestResponse<?> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication);

    RestResponse<?> withdrawMoney(DepositMoneyDTO withdrawMoneyDTO, Authentication authentication);

    RestResponse<?> transferMoney(TransactionDTO transferMoneyDTO, Authentication authentication);

    RestResponse<ResponsePageCard> listTransactionByCardAndType(FilterTransactionDTO listTransactionDTO,
                                                                Authentication authentication);

    RestResponse<ResponsePageCard> listAllTransactionByCard(FilterTransactionDTO listTransactionPagingRequest,
                                                            Authentication authentication);

    RestResponse<ResponsePageCard> listTransaction(FilterTransactionDTO filterTransactionDTO,
                                                   Authentication authentication);

    RestResponse<ResponsePageCard> listAdminTransaction(FilterTransactionDTO filterTransactionDTO);
}
