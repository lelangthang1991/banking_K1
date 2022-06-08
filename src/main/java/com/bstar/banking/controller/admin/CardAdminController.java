package com.bstar.banking.controller.admin;

import com.bstar.banking.model.request.ActivateCardDTO;
import com.bstar.banking.model.request.FilterCardDTO;
import com.bstar.banking.model.request.PagingRequest;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@PreAuthorize("hasAuthority('0')")
@RequestMapping("/api/v1/admin/cards")
@RestController
public class CardAdminController {
    private final CardService cardService;

    public CardAdminController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/get-page")
    public ResponseEntity<RestResponse<ResponsePageCard>> getPageCard(PagingRequest page) {
        return ResponseEntity.ok(cardService.findPageCard(page));
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<RestResponse<?>> getCardDetail(@Valid @NotBlank @PathVariable("cardNumber") String cardNumber) {
        return ResponseEntity.ok(cardService.findCardByCardNumber(cardNumber));
    }
    @DeleteMapping("/{cardNumber}")
    public ResponseEntity<RestResponse<?>> cardDisabled(@Valid @NotBlank @PathVariable("cardNumber") String cardNumber) {
        return ResponseEntity.ok(cardService.cardDisabled(cardNumber));
    }

    @GetMapping("/get-all-card")
    public ResponseEntity<RestResponse<?>> findAllCardFiltered(@Valid FilterCardDTO cardDTO) {
        return ResponseEntity.ok(cardService.findAllCardFiltered(cardDTO));
    }

    @PostMapping("/activate-card")
    public ResponseEntity<RestResponse<?>> activatedCard(@Valid @RequestBody ActivateCardDTO cardDTO, Authentication authentication) {
        return ResponseEntity.ok(cardService.activatedCard(cardDTO, authentication));
    }
}
