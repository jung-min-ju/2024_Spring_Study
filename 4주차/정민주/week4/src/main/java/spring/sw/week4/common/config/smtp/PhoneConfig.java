package spring.sw.week4.common.config.smtp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sms")
public class PhoneConfig {
    private String apiKey;
    private String secretKey;
    private String adminPhone;
}
