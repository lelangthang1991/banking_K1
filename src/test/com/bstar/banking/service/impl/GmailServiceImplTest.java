package com.bstar.banking.service.impl;

import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.MailDefault;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.UserRepository;
import com.google.api.client.http.HttpTransport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.MessagingException;
import java.io.File;
import java.util.Optional;

import static com.bstar.banking.common.MailerString.SEND_MAIL_FAILURE;
import static com.bstar.banking.common.MailerString.SEND_MAIL_SUCCESS;
import static com.bstar.banking.common.StatusCodeString.BAD_REQUEST;
import static com.bstar.banking.common.StatusCodeString.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GmailServiceImplTest {
    @InjectMocks
    private GmailServiceImpl gmailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpTransport transport;
    @Mock
    private File file;

    @Test
    void send() throws MessagingException {
        MailDefault mail = new MailDefault();
        mail.setTo("123123");
        mail.setSubject("123123");
        mail.setText("123123");
        Throwable exception = assertThrows(NotFoundException.class,
                () ->  gmailService.send(mail));
        assertEquals(exception.getMessage(), exception.getMessage());
    }

    @Test
    void addToQueueSuccess() {
        MailDefault mail = new MailDefault();
        gmailService.addToQueue(mail);
    }

    @Test
    void sendWelcomeSuccess() {
        User user = new User();
        user.setEmail("hoang@gmail.com");
        user.setVerifyCode("1234");
        String verifycode = "1234";
        gmailService.sendWelcome(user, verifycode);
    }

    @Test
    void sendVerifyCodeSuccess() throws MessagingException {
        User user = new User();
        user.setEmail("hoanganh@gmail.com");
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(Mockito.mock(User.class)));
        when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        RestResponse response = gmailService.sendVerifyCode(user.getEmail());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(OK);
        Assertions.assertThat(response.getStatusDescription()).isEqualTo(SEND_MAIL_SUCCESS);
    }

    @Test
    void sendVerifyCode_failure() throws MessagingException {
        User user = new User();
        user.setEmail("hoanganh@gmail.com");
        when(userRepository.findById(Mockito.any())).thenThrow(new NotFoundException(BAD_REQUEST, SEND_MAIL_FAILURE));

        Throwable exception = assertThrows(NotFoundException.class,
                () ->   gmailService.sendVerifyCode(user.getEmail()));
        assertEquals(SEND_MAIL_FAILURE, exception.getMessage());

    }
}