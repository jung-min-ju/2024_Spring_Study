package spring.sw.week4.crossdomain.auth.localCache;
import org.springframework.stereotype.Component;
import spring.sw.week4.common.exception.collections.business.business.NotMatchAuthCodeException;
import spring.sw.week4.common.exception.collections.business.business.UnVerifiedInfoException;
import spring.sw.week4.crossdomain.auth.dto.VerificationInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static spring.sw.week4.common.config.constant.InitConstants.completeVerification;

@Component
public class LocalCache {
    // 이메일 인증 정보 저장
    private final Map<String, String> emailCache = new ConcurrentHashMap<>();
    // 전화번호 인증 정보 저장
    private final Map<String, String> phoneCache = new ConcurrentHashMap<>();
    // 비밀번호 재확인 정보 저장
    private final Map<String, String> passwordCache = new ConcurrentHashMap<>();

    // 이메일 인증 캐시 반환
    public Map<String, String> getEmailCache() {
        return emailCache;
    }

    // 전화번호 인증 캐시 반환
    public Map<String, String> getPhoneCache() {
        return phoneCache;
    }

    // 비밀번호 인증 캐시 반환
    public Map<String, String> getPasswordCache() {
        return passwordCache;
    }

    // 인증번호 저장
    public void storeVerificationCode(Map<String, String> cache, String key, String value) {
        cache.put(key, value);
    }

    // 인증 번호 확인 및 상태 업데이트
    public void verifyCode (Map<String, String> cache, String key, String inputCode) {

        // 1. 캐시 내에 key가 존재 하는지 확인
        isKeyPresent(cache, key);

        // 2. key에 해당하는 인증 번호 값 가져오기
        String storedCode = cache.get(key);

        // 3. 인증 번호 다를 때
        if (!storedCode.equals(inputCode)) {
            throw new NotMatchAuthCodeException();
        }
        cache.put(key, completeVerification);
    }

    // 인증 상태 확인
    public void isVerified (Map<String, String> cache, String key) {
        // 캐시 내에 key가 존재 하는지 확인
        isKeyPresent(cache, key);
        if(!completeVerification.equals(cache.get(key))) {
            throw new UnVerifiedInfoException();
        }
    }

    // 캐시에서 이메일, 전화번호, 비밀번호 정보를 한 번에 삭제
    public void clearCache (VerificationInfo info) {
        emailCache.remove(info.email());
        phoneCache.remove(info.phone());
        passwordCache.remove(info.password());
    }

    // 캐시 내에 key가 존재 하는지 확인하는 메서드
    private void isKeyPresent (Map<String, String> cache, String key) {
        if (!cache.containsKey(key)) {
            throw new UnVerifiedInfoException();
        }
    }
}
