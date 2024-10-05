package spring.sw.week4.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import spring.sw.week4.crossdomain.auth.dto.CheckPasswordReqDto;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class UtilService {
    private final ResourceLoader resourceLoader;

    public String getRandomNum() { //인증번호 전송 시 쓰일 함수
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000,900000));
    }

    public String getRandomUUID(String originalName) { //나중에 s3 사용시 쓰일 함수
        String uuid = UUID.randomUUID().toString();
        return uuid+originalName;
    }

    public boolean checkPassword(CheckPasswordReqDto checkPasswordReqDto) {
        return checkPasswordReqDto.password().equals(checkPasswordReqDto.checkPassword());
    }

}
