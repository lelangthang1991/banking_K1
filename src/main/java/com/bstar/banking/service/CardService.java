package com.bstar.banking.service;

import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import org.springframework.security.core.Authentication;

public interface CardService {
    RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication);

    RestResponse<ResponsePageCard> findCardByKeyword(PagingRequest request);

    RestResponse<ResponsePageCard> findCardByKeywordAndActivated(PagingRequest request);

    RestResponse<?> findCardByEmail(String email);

    RestResponse<ResponsePageCard> findPageCard(PagingRequest request);

    RestResponse<?> findCardByCardNumber(String cardNumber);

    RestResponse<?> cardUpdate(CardDTO cardDTO, Authentication authentication);

    RestResponse<?> cardDisabled(String cardNumber);

    RestResponse<?> bankRegister(RegisterBankCardRq bankRequest, Authentication authentication);

    RestResponse<?> adminBankRegister(AdminRegisterDTO registerDTO, Authentication authentication);


    RestResponse<?> changePinCode(ChangePinCodeDTO changePinCodeDTO, Authentication authentication);

    RestResponse<?> findAllCardFiltered(FilterCardDTO filterCardDTO);
    RestResponse<?> activatedCard(ActivateCardDTO cardDTOs);
}
