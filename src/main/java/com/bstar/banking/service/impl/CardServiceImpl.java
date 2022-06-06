package com.bstar.banking.service.impl;

import com.bstar.banking.common.RandomBankNumber;
import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.exception.PinCodeException;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.ResponsePageCard;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.CardRepository;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.CardService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.bstar.banking.common.CardString.*;
import static com.bstar.banking.common.StatusCodeString.NOT_FOUND;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.*;

@Transactional
@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RestResponse<?> checkPinCode(PinCodeDTO pinCodeDTO, Authentication authentication) {
        User user = userRepository.getUserByEmail(authentication.getName()).orElseThrow(() -> new NotFoundException("404", "INVALID_EMAIL"));
        boolean isMatch = user.getCards().stream()
                .anyMatch(acc -> acc.getPinCode().equals(pinCodeDTO.getPinCode()) && acc.getCardNumber().equals(pinCodeDTO.getCardNumber()));
        if (isMatch) {
            return new RestResponse<>(OK, CARD_PIN_CODE_MATCH);
        } else {
            return new RestResponse<>(NOT_FOUND, CARD_PIN_CODE_DOES_NOT_MATCH);
        }
    }

    @Override
    public RestResponse<ResponsePageCard> findCardByKeyword(PagingRequest request) {
        if (StringUtils.isBlank(request.getSortField()) || StringUtils.isBlank(request.getSortDir())) {
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<Card> cardPage = cardRepository.findCardByKeyword(request.getKeyword(), pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    categoryDTOS.size(),
                    categoryDTOS));
        } else {
            Sort sort = Sort.by(request.getSortField());
            sort = request.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
            Page<Card> cardPage = cardRepository.findCardByKeyword(request.getKeyword(), pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    categoryDTOS.size(),
                    categoryDTOS));
        }
    }

    @Override
    public RestResponse<ResponsePageCard> findCardByKeywordAndActivated(PagingRequest request) {
        if (request.getIsActivated() == null) {
            throw new NotFoundException(CARD_ACTIVATE_PROPERTIES_NOT_FOUND);
        }
        if (StringUtils.isBlank(request.getSortField()) || StringUtils.isBlank(request.getSortDir())) {
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());
            Page<Card> cardPage = cardRepository.findCardByKeywordAndActivated(request.getKeyword(), request.getIsActivated(), pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    cardPage.getTotalElements(),
                    categoryDTOS));
        } else {
            Sort sort = Sort.by(request.getSortField());
            sort = request.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);
            Page<Card> cardPage = cardRepository.findCardByKeywordAndActivated(request.getKeyword(), request.getIsActivated(), pageable);
            List<CardDTO> categoryDTOS = cardPage.getContent()
                    .parallelStream()
                    .map(card -> modelMapper.map(card, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
                    cardPage.getSize(),
                    cardPage.getTotalPages(),
                    cardPage.getTotalElements(),
                    categoryDTOS));
        }
    }

    @Override
    public RestResponse<?> findCardByEmail(String email) {
        List<CardDTO> cardDTOS = cardRepository.findCardByEmail(email)
                .stream()
                .map(card -> modelMapper.map(card, CardDTO.class))
                .collect(Collectors.toList());
        return new RestResponse<>(OK,
                GET_LIST_CARD_SUCCESS,
                cardDTOS);
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
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
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
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(cardPage.getNumber(),
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
    public RestResponse<?> cardUpdate(CardDTO cardDTO, Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
        Card card = cardRepository.findById(cardDTO.getCardNumber())
                .orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        card.setBalance(cardDTO.getBalance());
        if (card.getPinCode().equals(cardDTO.getPinCode())) {
            throw new PinCodeException(NEW_PIN_CODE_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE);
        }
        card.setPinCode(cardDTO.getPinCode());
        card.setCardType(cardDTO.getCardType());
        card.setIsActivated(cardDTO.getIsActivated());
        card.setUpdatePerson(user.getEmail());
        card.setUpdateDate(new Date());
        Card cardSave = cardRepository.save(card);
        return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, modelMapper.map(cardSave, CardDTO.class));
    }

    @Override
    public RestResponse<?> cardDisabled(String cardNumber) {
        Card card = cardRepository.findById(cardNumber)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, CARD_NUMBER_NOT_FOUND));
        card.setIsActivated(false);
        cardRepository.save(card);
        return new RestResponse<>(OK, CARD_DISABLED_SUCCESS);
    }

    public RestResponse<?> bankRegister(@Valid @RequestBody RegisterBankCardRq registerDTO,
                                        Authentication authentication) {
        if (!registerDTO.getPinCode().equals(registerDTO.getConfirmPinCode())) {
            return new RestResponse<>(NOT_FOUND, PINCODE_DOES_NOT_MATCH);
        }
        String email = authentication.getName();
        User user = userRepository.findById(email).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        Card card = new Card();
        RandomBankNumber randomBankNumber = new RandomBankNumber();
        card.setCardType(registerDTO.getCardType());
        card.setCardNumber(randomBankNumber.randomBankNumber());
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
    public RestResponse<?> adminBankRegister(AdminRegisterDTO registerDTO, Authentication authentication) {
        if (!registerDTO.getPinCode().equals(registerDTO.getConfirmPinCode())) {
            return new RestResponse<>(NOT_FOUND, PINCODE_DOES_NOT_MATCH);
        }
        String email = authentication.getName();
        User user = userRepository.findById(registerDTO.getEmail()).orElseThrow(() -> new NotFoundException(EMAIL_NOT_FOUND));
        Card card = new Card();
        RandomBankNumber randomBankNumber = new RandomBankNumber();
        card.setCardType(registerDTO.getCardType());
        card.setCardNumber(randomBankNumber.randomBankNumber());
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
        if (card.getPinCode().equals(changePinCodeDTO.getNewPinCode())) {
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
            List<CardDTO> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        }
        else{
            Sort sort = Sort.by(filterCardDTO.getSortField());
            sort = filterCardDTO.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
            Pageable pageable = PageRequest.of(filterCardDTO.getPageNumber(), filterCardDTO.getPageSize(), sort);
            Page<Card> cards = cardRepository.findAllCardFiltered(filterCardDTO, pageable);
            List<CardDTO> cardDTOS = cards.stream()
                    .map(acc -> modelMapper.map(acc, CardDTO.class))
                    .collect(Collectors.toList());
            return new RestResponse<>(OK, GET_LIST_CARD_SUCCESS, new ResponsePageCard(
                    cards.getNumber(),
                    cards.getSize(),
                    cards.getTotalPages(),
                    cards.getTotalElements(),
                    cardDTOS));
        }

    }

    @Override
    public RestResponse<?> activatedCard(ActivateCardDTO cardDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        Card card = cardRepository.findById(cardDTO.getCardNumber()).orElseThrow(() -> new NotFoundException(CARD_NUMBER_NOT_FOUND));
        card.setIsActivated(true);
        card.setUpdateDate(new Date());
        card.setUpdatePerson(adminEmail);
        cardRepository.save(card);
        return new RestResponse<>(OK, CARD_ACTIVATE_SUCCESS);
    }

}
