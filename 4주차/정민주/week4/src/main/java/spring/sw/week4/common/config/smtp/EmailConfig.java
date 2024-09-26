package spring.sw.week4.common.config.smtp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties(prefix = "smtp.mail")
public class EmailConfig {
    private String host;
    private int port;
    private String adminMail;
    private String password;
    private boolean smtpAuth; //이 속성은 SMTP 서버에 인증이 필요한지 여부를 나타낸다.. 값이 true이면 메일을 보내기 전에 사용자 이름과 비밀번호를 이용해 서버에 로그인해야 한다. (true)
    private boolean smtpSslEnable; //SMTP 서버와의 연결에 SSL(Secure Sockets Layer)을 사용할지 여부를 나타낸다. 값이 true이면 데이터 전송이 암호화되어 보안이 강화된다. (false)
    private boolean smtpStarttlsEnable; //STARTTLS를 사용하여 TLS(Transport Layer Security) 연결을 시작할지 여부를 나타냅니다. STARTTLS는 암호화되지 않은 연결에서 TLS를 사용하여 암호화된 연결로 전환하는 프로토콜입니다. 값이 true이면 STARTTLS가 사용되며, 이 경우 메일 서버와의 모든 통신은 암호화됩니다.
    private String smtpSslTrust;

    @Bean
    public JavaMailSender javaMailSender(){ //JavaMailSender 객체를 생성하고, 이전에 설정한 정보를 이용해 이메일 서버 연결을 설정한 후, 이 객체를 반환함
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); //이 클래스는 메일 서버와의 연결 설정(host, port, username, password 등)을 관리합니다.
        mailSender.setHost(this.host);
        mailSender.setPort(this.port) ;
        mailSender.setUsername(this.adminMail);
        mailSender.setPassword(this.password);

        Properties props = mailSender.getJavaMailProperties(); //JavaMail API의 속성은 메일 서버와의 연결 방식(smtp, pop3, imap 등), 보안 설정(SSL, TLS 등), 인증 방식 등을 설정하는 데 사용
        props.put("mail.smtp.auth", this.smtpAuth);
        props.put("mail.smtp.ssl.enable", this.smtpSslEnable);
        props.put("mail.smtp.starttls.enable", this.smtpStarttlsEnable);
        props.put("mail.smtp.ssl.trust", smtpSslTrust);
        return mailSender;
    }
}
