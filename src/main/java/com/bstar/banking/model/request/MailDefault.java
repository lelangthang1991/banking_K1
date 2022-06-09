package com.bstar.banking.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Map;


@Data
@NoArgsConstructor
public class MailDefault {
    @NotBlank(message = "Email sender could not be null or empty")
    @Email(message = "Invalid sender email format")
    private String to;
    private String subject;
    private String text;
    @NotBlank(message = "Email receiver could not be null or empty")
    @Email(message = "Invalid receiver email format")
    private String from;
     private String cc;
     private String bcc;
     private String attachments;

    /**
     * Tạo Mail object từ các tham số:
     * @param to là email người nhận
     * @param subject là tiêu đề mail
     * @param text là nội dụng mail
     */
    public MailDefault(String to, String subject, String text) {
        this(to, subject, text, Map.of());
    }

    /**
     * Tạo Mail object từ các tham số:
     * @param to là email người nhận
     * @param subject là tiêu đề mail
     * @param text là nội dụng mail
     * @param others là Map&lt;String, String&gt; chứa các thông tin khác như from, cc, bcc, attatchments
     */
    public MailDefault(String to, String subject, String text, Map<String, String> others) {
        this.to = to;
        this.subject = subject;
        this.text = text;
        this.from = others.getOrDefault("from", "Web Banking <lehongcong.present@gmail.com>");
        this.cc = others.get("cc");
        this.bcc = others.get("bcc");
        this.attachments = others.get("attachments");
    }
}
