package spring.sw.week4.crossdomain.auth.smtp.service.phone;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoBadRequestException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.sw.week4.common.config.smtp.PhoneConfig;
import spring.sw.week4.common.exception.collections.business.business.FailedSendSmsToClient;
import spring.sw.week4.common.util.UtilService;
import spring.sw.week4.crossdomain.auth.localCache.LocalCache;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneAuthCodeReqDto;
import spring.sw.week4.crossdomain.auth.smtp.dto.phone.PhoneReqDto;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PhoneNumServiceImpl implements PhoneNumService{
    private final PhoneConfig phoneConfig;
    private final UtilService utilService;
    private DefaultMessageService messageService;
    private final LocalCache localCache;

    @PostConstruct //의존성 주입 끝난 후 자동호출
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
                phoneConfig.getApiKey(), phoneConfig.getSecretKey(), "https://api.coolsms.co.kr");
    }

    @Override
    public void sendVerifyNumberByPhoneNum(PhoneReqDto phoneReqDto) {
        //랜덤값 만들기
        String randomNum = utilService.getRandomNum();

        //1. sendOne()-> coolSMS 외부 api에 내용 담아 요청하는 함수
        Message message = new Message();
        message.setFrom(phoneConfig.getAdminPhone());
        message.setTo(phoneReqDto.phoneNum());
        message.setText("인증 번호 : " + randomNum);

        //2. SingleMessageSendingRequest -> 외부 api 요청 클래스
        //3. SingleMessageSentResponse -> 외부 api 응답 클래스
        SingleMessageSentResponse response;
        try {
            this.messageService.sendOne(new SingleMessageSendingRequest(message));
            // 성공적으로 응답을 받았을 때의 처리 로직
        } catch (Throwable throwable) {
            if (throwable instanceof NurigoBadRequestException) {
                // ValidationError 또는 FailedToAddMessage 에러 발생 시의 처리 로직
                throw new FailedSendSmsToClient();
            } else {
                // 그 외에 대한 에러 처리 로직
                throw new RuntimeException();
            }
        }

        //로컬 캐시에 저장
        localCache.storeVerificationCode(localCache.getPhoneCache(), phoneReqDto.phoneNum(), randomNum);
    }

    @Override
    public void checkVerifyNumberByPhoneNum(PhoneAuthCodeReqDto phoneAuthCodeReqDto) {
        String phoneNum = phoneAuthCodeReqDto.phoneNum();
        String code = phoneAuthCodeReqDto.phoneNumAuthCode();

        // 인증 번호 확인
        localCache.verifyCode(localCache.getPhoneCache(), phoneNum, code);
    }

}
