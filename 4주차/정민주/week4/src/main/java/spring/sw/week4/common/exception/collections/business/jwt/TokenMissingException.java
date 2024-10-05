package spring.sw.week4.common.exception.collections.business.jwt;

import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.jwt.TokenExceptionMessage.TokenMissingInHeader;

public class TokenMissingException extends BusinessException {
    public TokenMissingException() {
        super(TokenMissingInHeader);
    }

}
