package com.bstar.banking.service.impl;

import com.bstar.banking.common.RandomCardNumber;
import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.exception.PinCodeException;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.CheckCardNumberResponse;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.CardRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.CardService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.bstar.banking.common.CardString.*;
import static com.bstar.banking.common.StatusCodeString.NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.*;

@RequiredArgsConstructor
@Transactional
@Service
public class CardServiceImpl implements CardService {
    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException(NOT_FOUND, EMAIL_NOT_FOUND));
        boolean isMatch = user.getCards().stream()
                .anyMatch(acc -> pinCodeDTO != null &&
                        pinCodeDTO.getPinCode() != null &&
                        pinCodeDTO.getPinCode().equals(acc.getPinCode()) &&
                        pinCodeDTO.getCardNumber() != null &&
                        pinCodeDTO.getCardNumber().equals(acc.getCardNumber()));
        if (isMatch) {
            return new RestResponse<>(OK, CARD_PIN_CODE_MATCH);
        } else {
            throw new NotFoundException(NOT_FOUND, CARD_PIN_CODE_DOES_NOT_MATCH);
        }
    }

    @Override
    public RestResponse<?> findCardByEmail(String email) {
        List<CardDTO> cardDTOS = cardRepository.findCardByEmail(email)
                .stream()
                .map(card -> modelMapper.map(card, CardDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK, GET_CARD_SUCCESS, cardDTOS);
    }

    @Override
    public RestResponse<ResponsePageCard> findPageCard(PagingRequest request) {
        if (StringUtils.isBlank(request.getSortField()) || StringUtils.isBlank(request.getSortDir())) {
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<Card> cardPage = cardRepository.findAll(pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    cardPage.getTotalElements(),
                    categoryDTOS));
        } else {
            Sort sort = Sort.by(request.getSortField());
            sort = request.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
            Page<Card> cardPage = cardRepository.findAll(pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    cardPage.getTotalElements(),
                    categoryDTOS));
        }

    }

    @Override
    public RestResponse<?> findCardByCardNumber(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, CARD_NUMBER_NOT_FOUND));
        return new RestResponse<>(OK,
                GET_CARD_SUCCESS,
                modelMapper.map(card, CardDTO.class));
    }


    @Override
    public RestResponse<?> cardDisabled(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, CARD_NUMBER_NOT_FOUND));
        card.setIsActivated(false);
        cardRepository.save(card);
        return new RestResponse<>(OK, CARD_DISABLED_SUCCESS);
    }

    @Override
    public RestResponse<?> cardRegister(RegisterBankCardRq registerDTO,
                                        Authentication authentication) {
        if (!registerDTO.getPinCode().equals(registerDTO.getConfirmPinCode())) {
            throw new CompareException(PIN_CODE_DOES_NOT_MATCH);
        }
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        if (!(user.getCards().size() < 5)) {
            throw new CompareException(THE_NUMBER_OF_CARDS_ALLOWED_IS_EXCEEDED);
        }
        Card card = new Card();
        RandomCardNumber randomCardNumber = new RandomCardNumber();
        card.setCardType(1);
        card.setCardNumber("1001" + randomCardNumber.randomCardNumber());
        card.setBalance((double) 0);
        card.setPinCode(registerDTO.getPinCode());
        card.setIsActivated(true);
        card.setCreateDate(new Date());
        card.setCreatePerson(email);
        card.setUpdateDate(new Date());
        card.setUpdatePerson(email);
        card.setLevel(1);
        card.setDailyLimitAmount(100000000.00);
        card.setMonthlyLimitAmount(1000000000.00);
        card.setDailyAvailableTransfer(100000000.00);
        card.setMonthlyAvailableTransfer(1000000000.00);
        card.setUser(user);
        cardRepository.save(card);
        return new RestResponse<>(OK,
                CARD_REGISTRATION_SUCCESSFUL,
                modelMapper.map(card, CardDTO.class));

    }

    @Override
    public RestResponse<?> adminBankRegister(AdminRegisterDTO registerDTO, Authentication authentication) {
        if (!registerDTO.getPinCode().equals(registerDTO.getConfirmPinCode())) {
            throw new CompareException(PIN_CODE_DOES_NOT_MATCH);
        }
        String email = authentication.getName();
        User user = userRepository.findById(registerDTO.getEmail()).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        Card card = new Card();
        RandomCardNumber randomCardNumber = new RandomCardNumber();
        card.setCardType(registerDTO.getCardType());
        card.setCardNumber(randomCardNumber.randomCardNumber());
        card.setBalance((double) 0);
        card.setPinCode(registerDTO.getPinCode());
        card.setIsActivated(true);
        card.setCreateDate(new Date());
        card.setCreatePerson(email);
        card.setUpdateDate(new Date());
        card.setUpdatePerson(email);
        card.setUser(user);
        cardRepository.save(card);
        return new RestResponse<>(OK,
                CARD_REGISTRATION_SUCCESSFUL,
                modelMapper.map(card, CardDTO.class));
    }

    @Override
    public RestResponse<?> changePinCode(ChangePinCodeDTO changePinCodeDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Card card = user.getCards().stream()
                .filter(us -> us.getCardNumber().equals(changePinCodeDTO.getCardNumber()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        if (!changePinCodeDTO.getPinCode().equals(card.getPinCode())) {
            throw new PinCodeException(PIN_CODE_DOES_NOT_MATCH);
        }
        if (changePinCodeDTO.getNewPinCode().equals(card.getPinCode())) {
            throw new PinCodeException(NEW_PIN_CODE_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE);
        }
        card.setPinCode(changePinCodeDTO.getNewPinCode());
        cardRepository.save(card);
        return new RestResponse<>(OK,
                CARD_CHANGE_PIN_CODE_SUCCESSFUL,
                modelMapper.map(card, CardDTO.class));
    }

    @Override
    public RestResponse<?> findAllCardFiltered(FilterCardDTO filterCardDTO) {
        if (StringUtils.isBlank(filterCardDTO.getSortField()) || StringUtils.isBlank(filterCardDTO.getSortDir())) {
            Pageable pageable = PageRequest.of(filterCardDTO.getPageNumber(), filterCardDTO.getPageSize());
            Page<Card> cards = cardRepository.findAllCardFiltered(filterCardDTO, pageable);
//            Page<Card> cards = cardRepository.findAll(CardSpecifications.findAllCardFiltered(filterCardDTO), pageable);
            List<CardDTO> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_CARD_SUCCESS, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        } else {
            Sort sort = Sort.by(filterCardDTO.getSortField());
            sort = filterCardDTO.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(filterCardDTO.getPageNumber(), filterCardDTO.getPageSize(), sort);
            Page<Card> cards = cardRepository.findAllCardFiltered(filterCardDTO, pageable);
            List<CardDTO> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_PAGE_CARD_SUCCESS, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        }

    }

    @Override
    public RestResponse<?> activatedCard(ActivateCardDTO cardDTO, Authentication authentication) {
        String adminEmail = authentication.getName();
        Card card = cardRepository.findById(cardDTO.getCardNumber()).orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        card.setIsActivated(true);
        card.setUpdateDate(new Date());
        card.setUpdatePerson(adminEmail);
        cardRepository.save(card);
        return new RestResponse<>(OK, CARD_ACTIVATE_SUCCESS);
    }

    @Override
    public RestResponse<?> checkCardNumber(String cardNumber) {
        CheckCardNumberResponse cardNumberResponse = new CheckCardNumberResponse();
        Card card = cardRepository.findById(cardNumber).orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        cardNumberResponse.setFirstName(card.getUser().getFirstName());
        cardNumberResponse.setLastName(card.getUser().getLastName());
        cardNumberResponse.setIsActivated(card.getIsActivated());
        return new RestResponse<>(OK, GET_USER_INFO_SUCCESS, cardNumberResponse);
    }
}
