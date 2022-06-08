package com.bstar.banking.service;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface CardService {
    RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication);

    RestResponse<?> findCardByEmail(String email);

    RestResponse<ResponsePageCard> findPageCard(PagingRequest request);

    RestResponse<?> findCardByCardNumber(String cardNumber);

    RestResponse<?> cardDisabled(String cardNumber);

    RestResponse<?> cardRegister(RegisterBankCardRq bankRequest, Authentication authentication);

    RestResponse<?> changePinCode(ChangePinCodeDTO changePinCodeDTO, Authentication authentication);

    RestResponse<?> findAllCardFiltered(FilterCardDTO filterCardDTO);

    RestResponse<?> activatedCard(ActivateCardDTO cardDTOs, Authentication authentication);

    RestResponse<?> checkCardNumber(String cardNumber);

    RestResponse<?> adminBankRegister(AdminRegisterDTO registerDTO, Authentication authentication);


}
