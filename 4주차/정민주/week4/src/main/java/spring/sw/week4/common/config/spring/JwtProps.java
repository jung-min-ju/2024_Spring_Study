package spring.sw.week4.common.config.spring;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix="security.jwt")
@Data
@Validated
//이 어노테이션은 여러 종류의 유효성 검사 어노테이션(@Size, @Min, @NotBlank 등)을 사용할 수 있습니다.
//이 어노테이션들은 해당 필드의 값이 유효한지 검사하고, 유효하지 않은 경우 에러 메시지를 반환합니다.
public class JwtProps {
    @Size(min=60, message="Secret Key must be at least 60 characters long")
    private String secretKey;
    //JWT를 생성하고 검증할 때 사용되는 비밀 키입니다. 이 키는 JWT의 서명을 생성하고 검증하는 데 사용되며, 서버에서만 알고 있어야 합니다.

    @Min(value=10, message="Refresh_Token_Expiration_Minute must be at least 10 minuts long")
    private long refreshTokenExpirationMinutes;
    //'리프레시 토큰'의 유효 기간을 분 단위로 설정합니다. 리프레시 토큰은 액세스 토큰이 만료되었을 때 새로운 액세스 토큰을 발급받기 위해 사용됩니다.

    @Min(value=10, message="Access_Token_Expiration_Minute must be at least 10 minuts long")
    private long accessTokenExpirationMinutes;
    //'액세스 토큰'의 유효 기간을 분 단위로 설정합니다. 액세스 토큰은 사용자 인증 후 사용자의 권한을 확인하는 데 사용됩니다.

}

//이 코드는 JWT(JSON Web Token) 설정을 위한 JwtProps 클래스를 정의한 것입니다. JWT는 웹에서 사용하는 토큰 기반 인증 방식 중 하나로,
// 사용자의 정보를 암호화하여 토큰 형태로 제공합니다. 이 클래스에서는 JWT 생성 및 유효성 검사에 필요한 설정들을 관리합니다.