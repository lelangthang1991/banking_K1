package com.bstar.banking.service.impl;

import com.bstar.banking.entity.Account;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.PinCodeDTO;
import com.bstar.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;
    @InjectMocks
    private ModelMapper modelMapper;

    List<Account> list;

    @BeforeEach
    void setUp() {
        Account account1 =  new Account();
        list = Arrays.asList(account1);
        accountRepository.saveAll(list);
        accountService = new AccountServiceImpl(accountRepository, modelMapper);
    }

    @Test
    void WhenGetIdNotFound_shouldThrowException() {
        PinCodeDTO pinCodeDTO = new PinCodeDTO("123465789", "123456");
        when(accountRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> accountService.checkPinCode(pinCodeDTO)).isInstanceOf(NotFoundException.class);
        verify(accountRepository).findById(anyString());
    }
}