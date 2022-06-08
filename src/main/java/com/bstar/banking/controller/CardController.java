package com.bstar.banking.controller;

import com.bstar.banking.model.request.ChangePinCodeDTO;
import com.bstar.banking.model.request.PagingRequest;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.model.request.RegisterBankCardRq;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/check-pin")
    public RestResponse<?> checkPinCode(@Valid @RequestBody PinCodeDTO pinCodeDTO, Authentication authentication) {
        return cardService.checkPinCode(pinCodeDTO, authentication);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<RestResponse<?>> getCardByEmail(Authentication authentication) {
        return ResponseEntity.ok(cardService.findCardByEmail(authentication.getName()));
    }

    @GetMapping("/get-page")
    public ResponseEntity<RestResponse<ResponsePageCard>> getPageCard(@Valid PagingRequest page) {
        return ResponseEntity.ok(cardService.findPageCard(page));
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<RestResponse<?>> getCardDetail(@PathVariable("cardNumber") String cardNumber) {
        return ResponseEntity.ok(cardService.findCardByCardNumber(cardNumber));
    }

    @PutMapping("/change-pin-code")
    public ResponseEntity<RestResponse<?>> changePinCode(@Valid @RequestBody ChangePinCodeDTO changePinCodeDTO,
                                                         Authentication authentication) {
        return ResponseEntity.ok(cardService.changePinCode(changePinCodeDTO, authentication));
    }

    @PostMapping("/register")
    public ResponseEntity<RestResponse<?>> bankRegister(@Valid @RequestBody RegisterBankCardRq registerBankCardRq,
                                                        Authentication authentication) {
        return ResponseEntity.ok(cardService.cardRegister(registerBankCardRq, authentication));
    }

    @GetMapping("/check-card-info/{cardNumberBeneficiary}")
    public ResponseEntity<RestResponse<?>> checkCardNumber(@Valid @NotBlank @PathVariable("cardNumberBeneficiary") String cardNumberBeneficiary) {
        return ResponseEntity.ok(cardService.checkCardNumber(cardNumberBeneficiary));
    }
}
