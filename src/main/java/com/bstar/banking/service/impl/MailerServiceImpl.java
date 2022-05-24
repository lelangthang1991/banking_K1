package com.bstar.banking.service.impl;

import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.MailDefault;
import com.bstar.banking.model.response.CommonResponse;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.HeaderResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.MailerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.bstar.banking.common.MailerString.SEND_MAIL_SUCCESS;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class MailerServiceImpl implements MailerService {
    private final JavaMailSender sender;
    private final UserRepository userRepository;
    List<MailDefault> queue = new ArrayList<>();

    @Override
    public void send(MailDefault mail) throws MessagingException {
        MimeMessage msg = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText(), true);

        String from = mail.getFrom();
        if (from == null || from.trim().length() == 0) {
            from = "Banking <lehongcong.present@gmail.com>";
        } else if (!from.contains("<")) {
            from = String.format("%s <%s>", from, from);
        }
        helper.setFrom(from);
        helper.setReplyTo(from);

        String cc = mail.getCc();
        if (cc != null && cc.trim().length() > 0) {
            helper.setCc(cc);
        }

        String bcc = mail.getBcc();
        if (bcc != null && bcc.trim().length() > 0) {
            helper.setBcc(bcc);
        }

        String files = mail.getAttachments();
        if (files != null && files.trim().length() > 0) {
            Stream.of(files.split("[,;]+"))
                    .filter(filename -> filename.trim().length() > 0)
                    .map(filename -> new File(filename))
                    .forEach(file -> {
                        try {
                            helper.addAttachment(file.getName(), file);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
        }
        sender.send(msg);
    }

    @Override
    public void addToQueue(MailDefault mail) {
        queue.add(mail);
    }

    @Override
    public void sendWelcome(User account) {
        String url = "http://localhost:8080/account/activate/" + account.getEmail() + "?verify=";
        try {
            String to = account.getEmail();
            String text = "<hr><a href='" + url + "'>Kích hoạt tài khoản</a>";

            MailDefault mail = new MailDefault(to, "Welcome to Web Banking", text);
            this.addToQueue(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CommonResponse<ForgotPasswordResponse> sendVerifyCode(String email) {
        try {
            String verifyCode = RandomStringUtils.randomAlphabetic(6);
            String text = "Your verify code: " + "<strong>" + verifyCode + "</strong>";
            MailDefault mail = new MailDefault(email, "Verify Code", text);
            this.send(mail);
            User user = userRepository.findById(mail.getTo()).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            user.setVerifyCode(verifyCode);
            userRepository.save(user);
            CommonResponse<ForgotPasswordResponse> CommonResponse = new CommonResponse<>();
            CommonResponse.setHeader(new HeaderResponse());
            CommonResponse.setBody(new ForgotPasswordResponse("200", SEND_MAIL_SUCCESS));
            return CommonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            CommonResponse<ForgotPasswordResponse> CommonResponse = new CommonResponse<>();
            CommonResponse.setHeader(new HeaderResponse());
            CommonResponse.setBody(new ForgotPasswordResponse("404", e.getMessage()));
            return CommonResponse;
        }
    }
}
