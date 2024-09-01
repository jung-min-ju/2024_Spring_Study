package spring.sw.week4.crossdomain.auth.smtp.service.email;

import jakarta.mail.MessagingException;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailReqDto;

public interface EmailService {
    void sendVerifyNumberByEmail(EmailReqDto emailReqDto) throws MessagingException;
    void checkVerifyNumberByEmail(EmailAuthCodeReqDto emailAuthCodeReqDto);
}
