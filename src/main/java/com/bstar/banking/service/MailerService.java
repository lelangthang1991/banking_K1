package com.bstar.banking.service;


import com.bstar.banking.entity.User;
import com.bstar.banking.model.request.MailDefault;
import com.bstar.banking.model.response.RestResponse;

import javax.mail.MessagingException;


public interface MailerService {
    /**
     * Gửi email
     *
     * @param mail thông tin email cần gửi
     * @throws MessagingException lỗi gửi mail
     *                            return CommonResponse<ForgotPasswordResponse>
     */
    void send(MailDefault mail) throws MessagingException;

    /**
     * Xếp mail vào hàng đợi để gửi theo lịch trình
     *
     * @param mail thông tin email được xếp vào hàng đợi
     */
    void addToQueue(MailDefault mail);

    void sendWelcome(User card, String verifycode);

    RestResponse<?> sendVerifyCode(String email) throws MessagingException;
}