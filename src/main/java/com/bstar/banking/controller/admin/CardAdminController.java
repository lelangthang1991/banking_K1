package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.ActivateCardDTO;
import com.bstar.banking.model.request.PagingRequest;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasAuthority('0')")
@RequestMapping("/api/v1/admin/cards")
@RestController
public class CardAdminController {
    private final CardService cardService;

    public CardAdminController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/get-page")
    public RestResponse<ResponsePageCard> getPageCard(@Valid PagingRequest page) {
        return cardService.findPageCard(page);
    }

    @GetMapping("/{cardNumber}")
    public RestResponse<?> getCardDetail(@PathVariable("cardNumber") String cardNumber) {
        return cardService.findCardByCardNumber(cardNumber);
    }

    @GetMapping("/activated/find-by-keyword")
    public RestResponse<ResponsePageCard> findCardByKeywordActivated(@Valid PagingRequest page) {
        return cardService.findCardByKeywordAndActivated(page);
    }

    @GetMapping("/find-by-keyword")
    public RestResponse<ResponsePageCard> findCardByKeyword(@Valid PagingRequest page) {
        return cardService.findCardByKeyword(page);
    }

    @DeleteMapping("/{cardNumber}")
    public RestResponse<?> cardDisabled(@PathVariable("cardNumber") String cardNumber) {
        return cardService.cardDisabled(cardNumber);
    }

    @PostMapping("/activate-card")
    public ResponseEntity<RestResponse<?>> activatedCard(@Valid @RequestBody ActivateCardDTO cardDTO) {
        return ResponseEntity.ok(cardService.activatedCard(cardDTO));
    }
}
