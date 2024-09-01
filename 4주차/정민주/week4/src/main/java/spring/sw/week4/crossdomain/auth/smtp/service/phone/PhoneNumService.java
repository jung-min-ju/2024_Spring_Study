package spring.sw.week4.crossdomain.auth.smtp.service.phone;

import com.fasterxml.jackson.core.JsonProcessingException;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneReqDto;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public interface PhoneNumService {
    void sendVerifyNumberByPhoneNum( PhoneReqDto phoneReqDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException;
    void checkVerifyNumberByPhoneNum( PhoneAuthCodeReqDto phoneAuthCodeReqDto);
}
