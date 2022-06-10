package com.bstar.banking.service.impl;

import com.bstar.banking.entity.User;
import com.bstar.banking.model.request.DepositMoneyDTO;
import com.bstar.banking.model.request.SignupRequest;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.CardRepository;
import com.bstar.banking.repository.TransactionRepository;
import com.bstar.banking.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.PLEASE_CHECK_YOUR_EMAIL;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionImplTest {

    @InjectMocks
    private TransactionImpl transactionService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void deposit_money() throws Exception {
        //given
        DepositMoneyDTO depositMoneyDTO = new DepositMoneyDTO();
       depositMoneyDTO.setCardNumber("1001104089747116");
       depositMoneyDTO.setPinCode("1111");
       depositMoneyDTO.setAmount(50000.0);




        //when


        //then


    }

    @Test
    void deposit_money_falure() throws Exception {

    }

    @Test
    void withDraw_money() throws Exception {

    }

    @Test
    void withDraw_money_falure() throws Exception {

    }

    @Test
    void transfer_money() throws Exception {

    }

    @Test
    void transfer_money_falure() throws Exception {

    }


}
