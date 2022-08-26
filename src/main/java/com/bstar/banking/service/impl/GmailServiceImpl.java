package com.bstar.banking.service.impl;

import com.bstar.banking.entity.User;
import com.bstar.banking.exception.NotFoundException;
import com.bstar.banking.model.request.MailDefault;
import com.bstar.banking.model.response.ForgotPasswordResponse;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.repository.UserRepository;
import com.bstar.banking.service.MailerService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

import static com.bstar.banking.common.MailerString.SEND_MAIL_FAILURE;
import static com.bstar.banking.common.MailerString.SEND_MAIL_SUCCESS;
import static com.bstar.banking.common.StatusCodeString.BAD_REQUEST;
import static com.bstar.banking.common.StatusCodeString.OK;
import static com.bstar.banking.common.UserString.GET_USER_EMAIL_NOT_FOUND;

@Service
public class GmailServiceImpl implements MailerService {
    private final UserRepository userRepository;
    List<MailDefault> queue = new ArrayList<>();

    private static final String APPLICATION_NAME = "OMS GMAIL API";
    private final JsonFactory JSON_FACTORY = Utils.getDefaultJsonFactory();
    private static final HttpTransport HTTP_TRANSPORT;
    private static final java.io.File CREDENTIALS_FOLDER;
    private static final FileDataStoreFactory DATA_STORE_FACTORY;
    private static final String CLIENT_SECRET_FILE_NAME = "/client.secret.json";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    static {
        try {
            String srcPath = "src/main/resources/credentials";
            String rootPath = Paths.get("@").toAbsolutePath().toString().replace("\\", "/");
            String absPath = rootPath.replace("@", srcPath);
            CREDENTIALS_FOLDER = new java.io.File(absPath);
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
        } catch (GeneralSecurityException | IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public GmailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Properties loadProperties(String fileName) throws IOException {
        try (InputStream input = GmailServiceImpl.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties prop = new Properties();
            if (input == null) {
                throw new IOException();
            }
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    public Credential getCredentials() {
        try {
            java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
            if (!clientSecretFilePath.exists()) {
                throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
            }
            InputStream in = Files.newInputStream(clientSecretFilePath.toPath());
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void send(MailDefault mail) throws MessagingException {
        try {
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials())
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            Properties mailCredential = GmailServiceImpl.loadProperties("application.properties");
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage mailContent = new MimeMessage(session);
            mailContent.setFrom(new InternetAddress(mailCredential.getProperty("email")));
            mailContent.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(mail.getTo()));
            mailContent.setSubject(mail.getSubject(), "utf-8");
            mailContent.setContent(mail.getText(), MediaType.TEXT_HTML_VALUE);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            mailContent.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);
            service.users().messages().send("me", message).execute();
        } catch (GoogleJsonResponseException e) {
            throw new NotFoundException("Unable to send message: " + e.getDetails());
        } catch (MessagingException | IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void addToQueue(MailDefault mail) {
        queue.add(mail);
    }

    @Override
    public void sendWelcome(User card, String verifycode) {
        String url = "http://localhost:8080/api/v1/users/activate-user/" + card.getEmail() + "/" + verifycode;
        try {
            String to = card.getEmail();

            String text = "<hr><a href='" + url + "'>Click here to activate your card!</a>";

            MailDefault mail = new MailDefault(to, "Welcome to Web Banking", text);

            this.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RestResponse<?> sendVerifyCode(String email) throws MessagingException {
        try {
            String verifyCode = RandomStringUtils.randomAlphabetic(6);
            String text = "Your verify code: " + "<strong>" + verifyCode + "</strong>";
            MailDefault mail = new MailDefault(email, "Verify Code", text);
            this.send(mail);
            User user = userRepository.findById(mail.getTo()).orElseThrow(() -> new NotFoundException(GET_USER_EMAIL_NOT_FOUND));
            user.setVerifyCode(verifyCode);
            userRepository.save(user);
            return new RestResponse<>(OK, SEND_MAIL_SUCCESS, new ForgotPasswordResponse(user.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException(BAD_REQUEST, SEND_MAIL_FAILURE);
        }
    }
}
