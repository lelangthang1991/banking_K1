package com.bstar.banking.service;

import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    RestResponse<CommonResponse> depositMoney(DepositMoneyDTO depositMoneyDTO, Authentication authentication);
}
