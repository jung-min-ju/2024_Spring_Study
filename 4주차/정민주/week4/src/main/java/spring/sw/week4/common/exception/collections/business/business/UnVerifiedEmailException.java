package spring.sw.week4.common.exception.collections.business.business;

import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.business.AuthCodeExceptionMessage.UnVerifiedEmail;

public class UnVerifiedEmailException extends BusinessException {
    public UnVerifiedEmailException() {
        super( UnVerifiedEmail );
    }
}
