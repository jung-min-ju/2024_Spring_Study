package spring.sw.week4.common.exception.collections.business.business;

import spring.sw.week4.common.exception.collections.business.BusinessException;
import spring.sw.week4.common.exception.message.business.AuthCodeExceptionMessage;

public class NotMatchAuthCodeException extends BusinessException {
    public NotMatchAuthCodeException() {
        super(AuthCodeExceptionMessage.NotMatchAuthCodeByEmail);
    }
}
