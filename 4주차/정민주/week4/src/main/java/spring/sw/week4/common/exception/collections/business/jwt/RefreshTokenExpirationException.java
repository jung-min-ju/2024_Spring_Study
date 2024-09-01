package spring.sw.week4.common.exception.collections.business.jwt;


import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.jwt.TokenExceptionMessage.RefreshTokenExpired;

public class RefreshTokenExpirationException extends BusinessException {
    public RefreshTokenExpirationException() {
        super(RefreshTokenExpired);
    }
}
