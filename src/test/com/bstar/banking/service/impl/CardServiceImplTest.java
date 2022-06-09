package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Card;
import com.bstar.banking.entity.User;
import com.bstar.banking.exception.CompareException;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.exception.PinCodeException;
import com.bstar.banking.model.request.*;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.CardRepository;
import com.bstar.banking.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static com.bstar.banking.common.CardString.*;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.GET_USER_INFO_SUCCESS;
import static com.bstar.banking.common.UserString.PIN_CODE_DOES_NOT_MATCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {
    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(cardRepository, userRepository, modelMapper);
    }

    @AfterEach
    void tearDown() {
        cardRepository.deleteAll();
    }

    @Test
    void givenCardAndUserAndPinCodeDTO_whenGetUserByEmail_thenShouldGetStatusOK() {
        //given
        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setPinCode("1234");
        User user = new User();
        user.setEmail("hoanganh25022000@gmail.com");
        user.setCards(List.of(card));
        PinCodeDTO pinCodeDTO = new PinCodeDTO();
        pinCodeDTO.setCardNumber("12345678910");
        pinCodeDTO.setPinCode("1234");

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        RestResponse<?> response = cardService.checkPinCode(pinCodeDTO, mock(Authentication.class));
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_PIN_CODE_MATCH);
    }

    @Test
    void givenCardAndUserAndPinCodeDTO_whenGetUserByEmail_thenShouldGetCartNumberDoesNotMatch() {
        //given
        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setPinCode("1234");
        User user = new User();
        user.setEmail("hoanganh25022000@gmail.com");
        user.setCards(List.of(card));
        PinCodeDTO pinCodeDTO = new PinCodeDTO();
        pinCodeDTO.setCardNumber("32165498713");
        pinCodeDTO.setPinCode("1234");

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () -> cardService.checkPinCode(pinCodeDTO, mock(Authentication.class)));
        assertEquals(CARD_PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenCardAndUserAndPinCodeDTO_whenGetUserByEmail_thenShouldGetPinCodeDoesNotMatch() {
        //given
        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setPinCode("1234");
        User user = new User();
        user.setEmail("hoanganh25022000@gmail.com");
        user.setCards(List.of(card));
        PinCodeDTO pinCodeDTO = new PinCodeDTO();
        pinCodeDTO.setCardNumber("12345678910");
        pinCodeDTO.setPinCode("1111");

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () -> cardService.checkPinCode(pinCodeDTO, mock(Authentication.class)));
        assertEquals(CARD_PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenCardAndUserAndPinCodeDTO_whenGetUserByEmail_thenShouldGetPinCodeAndCartNumberDoesNotMatch() {
        //given
        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setPinCode("1234");
        User user = new User();
        user.setEmail("hoanganh25022000@gmail.com");
        user.setCards(List.of(card));
        PinCodeDTO pinCodeDTO = new PinCodeDTO();
        pinCodeDTO.setCardNumber("123324654");
        pinCodeDTO.setPinCode("1111");

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () -> cardService.checkPinCode(pinCodeDTO, mock(Authentication.class)));
        assertEquals(CARD_PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenCardAndUserAndPinCodeDTO_whenGetUserByEmail_thenShouldGetPinCodeAndCartNumberIsNull() {
        //given
        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setPinCode("1234");
        User user = new User();
        user.setEmail("hoanganh25022000@gmail.com");
        user.setCards(List.of(card));
        PinCodeDTO pinCodeDTO = new PinCodeDTO();
        pinCodeDTO.setCardNumber(null);
        pinCodeDTO.setPinCode(null);

        //when
        when(userRepository.getUserByEmail(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(NotFoundException.class,
                () -> cardService.checkPinCode(pinCodeDTO, mock(Authentication.class)));
        assertEquals(CARD_PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }


    @Test
    void givenCardAndUser_whenFindCardByEmail_thenShouldFoundEmail() {
        //given
        String email = "hoanganh25022000@gmail.com";
        User user = new User();
        user.setEmail(email);

        Card card = new Card();
        card.setCardNumber("12345678910");
        card.setUser(user);

        //when
        when(cardRepository.findCardByEmail(Mockito.any())).thenReturn(Optional.of(card));

        //then
        RestResponse response = cardService.findCardByEmail(email);
        Assertions.assertThat(response.getStatusCode()).isEqualTo("200");
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_CARD_SUCCESS);
    }

    @Test
    void givenPageCard_whenFindAllByPageable_thenShouldPageCard() {
        //given
        Page<Card> page = Mockito.mock(Page.class);
        PagingRequest request = new PagingRequest();
        request.setPageNumber(0);
        request.setPageSize(10);
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize());

        //when
        when(cardRepository.findAll(pageable)).thenReturn(page);

        //then
        RestResponse response = cardService.findPageCard(request);
        Assertions.assertThat(response.getStatusCode()).isEqualTo("200");
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_CARD_SUCCESS);
    }

    @Test
    void givenPageCardWithSort_whenFindAllByPageable_thenShouldReturnStatusCodeOkAndGetPageCardSuccess() {
        //given
        Page<Card> page = Mockito.mock(Page.class);
        PagingRequest request = new PagingRequest();
        request.setPageNumber(0);
        request.setPageSize(10);
        request.setSortDir("asc");
        request.setSortField("cardNumber");
        Sort sort = Sort.by(request.getSortField());
        sort = request.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), sort);

        //when
        when(cardRepository.findAll(pageable)).thenReturn(page);

        //then
        RestResponse response = cardService.findPageCard(request);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_CARD_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();

    }

    @Test
    void givenCard_whenFindCardById_thenShouldReturnStatusCodeOkAndGetCardSuccess() {
        //given
        Card card = new Card();
        card.setCardNumber("12312312");

        // when
        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));

        //then
        RestResponse response = cardService.findCardByCardNumber("132464654687");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_CARD_SUCCESS);
    }


    @Test
    void givenCardNumber_whenCardFindByIdAndCardSave_thenShouldReturnOkAndCardDisabledSuccess() {
        //given
        Card card = new Card();
        card.setCardNumber("12312312");

        // when
        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        //then
        RestResponse response = cardService.cardDisabled("12312312");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_DISABLED_SUCCESS);
    }

    @Test
    void givenRegisterDTO_whenFindByIdAndSave_thenShouldReturnOkAndCardRegisSuccess() {
        //given
        RegisterBankCardRq registerDTO = new RegisterBankCardRq();
        registerDTO.setCardType(1);
        registerDTO.setPinCode("1234");
        registerDTO.setConfirmPinCode("1234");
        Card card = new Card();
        User user = new User();
        user.setEmail("hoanganh25213@gmail.com");
        user.setCards(List.of(card));

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(cardRepository.save(Mockito.any())).thenReturn(card);

        //then
        RestResponse response = cardService.cardRegister(registerDTO, Mockito.mock(Authentication.class));
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_REGISTRATION_SUCCESSFUL);
    }

    @Test
    void givenRegisterDTO_whenFindByIdAndSave_thenShouldThrowErrorTheNumberOfCardAllowIsExceeded() {
        //given
        RegisterBankCardRq registerDTO = new RegisterBankCardRq();
        registerDTO.setCardType(1);
        registerDTO.setPinCode("1234");
        registerDTO.setConfirmPinCode("1234");
        Card card = new Card();
        Card card1 = new Card();
        Card card2 = new Card();
        Card card3 = new Card();
        Card card4 = new Card();
        Card card5 = new Card();
        User user = new User();
        user.setEmail("hoanganh25213@gmail.com");
        user.setCards(List.of(card, card1, card2, card3, card4, card5));

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(CompareException.class,
                () -> cardService.cardRegister(registerDTO, Mockito.mock(Authentication.class)));
        assertEquals(THE_NUMBER_OF_CARDS_ALLOWED_IS_EXCEEDED, exception.getMessage());
    }

    @Test
    void givenRegisterDTO_thenShouldReturnPinCodeDoesNotMatch() {
        //given
        RegisterBankCardRq registerDTO = new RegisterBankCardRq();
        registerDTO.setCardType(1);
        registerDTO.setPinCode("1234");
        registerDTO.setConfirmPinCode("1235");

        //then
        Throwable exception = assertThrows(CompareException.class,
                () -> cardService.cardRegister(registerDTO, Mockito.mock(Authentication.class)));
        assertEquals(PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenCardAndUserAndChangePinCodeDTO_whenUserFindByIdAndSaveCard_thenShouldReturnStatusCodeOkAndChangeSuccessful() {
        //given
        Card card = new Card();
        card.setCardNumber("123456789");
        card.setPinCode("1111");
        User user = new User();
        user.setEmail("hoanganh25213@gmail.com");
        user.setCards(List.of(card));

        ChangePinCodeDTO changePinCodeDTO = new ChangePinCodeDTO();
        changePinCodeDTO.setCardNumber("123456789");
        changePinCodeDTO.setPinCode("1111");
        changePinCodeDTO.setNewPinCode("1234");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(cardRepository.save(Mockito.any())).thenReturn(card);

        //then
        RestResponse response = cardService.changePinCode(changePinCodeDTO, Mockito.mock(Authentication.class));
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_CHANGE_PIN_CODE_SUCCESSFUL);
    }

    @Test
    void givenCardAndUserAndChangePinCodeDTO_whenUserFindById_thenShouldThrowErrorPinCodeDoesNotMatch() {
        //given
        Card card = new Card();
        card.setCardNumber("123456789");
        card.setPinCode("1111");
        User user = new User();
        user.setEmail("hoanganh25213@gmail.com");
        user.setCards(List.of(card));

        ChangePinCodeDTO changePinCodeDTO = new ChangePinCodeDTO();
        changePinCodeDTO.setCardNumber("123456789");
        changePinCodeDTO.setPinCode("1234");
        changePinCodeDTO.setNewPinCode("1234");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(PinCodeException.class,
                () -> cardService.changePinCode(changePinCodeDTO, Mockito.mock(Authentication.class)));
        assertEquals(PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenCardAndUserAndChangePinCodeDTO_whenUserFindById_thenShouldThrowErrorNewPinCodeCanNotBeTheSameOne() {
        //given
        Card card = new Card();
        card.setCardNumber("123456789");
        card.setPinCode("1111");
        User user = new User();
        user.setEmail("hoanganh25213@gmail.com");
        user.setCards(List.of(card));

        ChangePinCodeDTO changePinCodeDTO = new ChangePinCodeDTO();
        changePinCodeDTO.setCardNumber("123456789");
        changePinCodeDTO.setPinCode("1111");
        changePinCodeDTO.setNewPinCode("1111");

        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        //then
        Throwable exception = assertThrows(PinCodeException.class,
                () -> cardService.changePinCode(changePinCodeDTO, Mockito.mock(Authentication.class)));
        assertEquals(NEW_PIN_CODE_CAN_NOT_BE_THE_SAME_AS_THE_OLD_ONE, exception.getMessage());
    }

    @Test
    void givenCardDTOWithPageNumberAndPageSize_whenFindAllCardFiltered_thenShouldReturnOkAndGetPageCardSuccess() {
        //given
        Page<Card> cards = Mockito.mock(Page.class);
        FilterCardDTO filterCardDTO = new FilterCardDTO();
        filterCardDTO.setPageNumber(0);
        filterCardDTO.setPageSize(10);
        Pageable pageable = PageRequest.of(filterCardDTO.getPageNumber(), filterCardDTO.getPageSize());

        //when
        when(cardRepository.findAllCardFiltered(filterCardDTO, pageable)).thenReturn(cards);

        //then
        RestResponse<?> response = cardService.findAllCardFiltered(filterCardDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_CARD_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void givenCardDTOWithPageNumberAndPageSizeAndSortFieldAndSortDir_whenFindAllCardFiltered_thenShouldReturnOkAndGetPageCardSuccess() {
        //given
        Page<Card> cards = Mockito.mock(Page.class);
        FilterCardDTO filterCardDTO = new FilterCardDTO();
        filterCardDTO.setPageNumber(0);
        filterCardDTO.setPageSize(10);
        filterCardDTO.setSortDir("asc");
        filterCardDTO.setSortField("cardNumber");
        Sort sort = Sort.by(filterCardDTO.getSortField());
        sort = filterCardDTO.getSortDir().equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(filterCardDTO.getPageNumber(), filterCardDTO.getPageSize(), sort);

        //when
        when(cardRepository.findAllCardFiltered(filterCardDTO, pageable)).thenReturn(cards);

        //then
        RestResponse<?> response = cardService.findAllCardFiltered(filterCardDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_PAGE_CARD_SUCCESS);
        Assertions.assertThat(response.getData()).isNotNull();
    }

    @Test
    void givenCardDTOAndCard_thenCardFindByIdAndSaveCard_thenShouldReturnOkAndCardActivatedSuccess() {
        //given
        Authentication authentication = Mockito.mock(Authentication.class);
        String updatePerson = authentication.getName();
        ActivateCardDTO cardDTO = new ActivateCardDTO();
        cardDTO.setCardNumber("1234");
        Card card = new Card();
        card.setCardNumber("1234");
        card.setUpdatePerson(updatePerson);

        //when
        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(cardRepository.save(Mockito.any())).thenReturn(card);

        //then
        RestResponse response = cardService.activatedCard(cardDTO, Mockito.mock(Authentication.class));
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_ACTIVATE_SUCCESS);
    }

    @Test
    void givenCardNumber_whenCardFindById_thenShouldReturnOkAndGetUserInfoSuccess() {
        //givem
        Card card = new Card();
        card.setCardNumber("1234");
        //when
        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));

        //then
        RestResponse response = cardService.checkCardNumber("1234");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(GET_USER_INFO_SUCCESS);
    }

    @Test
    void givenAdminRegisterDTO_thenShouldReturnPinCodeDoesNotMatch() {
        //given
        AdminRegisterDTO registerDTO = new AdminRegisterDTO();
        registerDTO.setPinCode("1234");
        registerDTO.setConfirmPinCode("1235");

        //then
        Throwable exception = assertThrows(CompareException.class,
                () -> cardService.adminBankRegister(registerDTO, mock(Authentication.class)));
        assertEquals(PIN_CODE_DOES_NOT_MATCH, exception.getMessage());
    }

    @Test
    void givenAdminRegisterDTO_when_thenShouldReturnOkAndCardRegisterSuccess() {
        //given
        AdminRegisterDTO registerDTO = new AdminRegisterDTO();
        registerDTO.setPinCode("1234");
        registerDTO.setConfirmPinCode("1234");
        User user = new User();
        Card card = new Card();
        //when
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        when(cardRepository.save(Mockito.any())).thenReturn(card);

        //then
        RestResponse response = cardService.adminBankRegister(registerDTO, Mockito.mock(Authentication.class));
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(CARD_REGISTRATION_SUCCESSFUL);
    }
}