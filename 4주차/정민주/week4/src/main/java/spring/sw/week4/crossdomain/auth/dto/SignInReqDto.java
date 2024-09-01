package spring.sw.week4.crossdomain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignInReqDto (

        @NotBlank(message = "이메일은 필수 값 입니다.")
        @Email(message = "유효한 이메일 주소를 입력해 주세요.")
        @Size(min = 5, max = 254, message = "5이상, 64이하 이메일을 입력해 주세요.")
        String email,

        @NotBlank(message="비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$", message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
        String password
)
{}
