package spring.sw.week4.crossdomain.auth.smtp.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sw.week4.common.util.UtilService;
import spring.sw.week4.crossdomain.auth.localCache.LocalCache;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.email.EmailReqDto;

@RequiredArgsConstructor
@Service
@Transactional
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final UtilService utilService;
    private final LocalCache localCache;

    @Value("${smtp.mail.adminMail}")  // 프로퍼티에서 adminMail 값을 직접 주입
    private String adminMail;

    @Override
    public void sendVerifyNumberByEmail(EmailReqDto emailReqDto) throws MessagingException {
        String randomNum = utilService.getRandomNum();
        String email = emailReqDto.email();

        // 이메일로 인증번호 전송
        sendEmail(email, "이메일 인증",
                String.format("<p>인증번호 : <span style=\"color:red\">%s<span></p>", randomNum)
        );

        // 인증번호 저장
        localCache.storeVerificationCode(localCache.getEmailCache(), email, randomNum);
    }

    private void sendEmail(String email, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(adminMail);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        this.javaMailSender.send(message);
    }

    @Override
    public void checkVerifyNumberByEmail(EmailAuthCodeReqDto emailAuthCodeReqDto) {
        String email = emailAuthCodeReqDto.email();
        String inputCode = emailAuthCodeReqDto.emailAuthCode();

        // 인증번호 확인
        localCache.verifyCode(localCache.getEmailCache(), email, inputCode);
    }

}
