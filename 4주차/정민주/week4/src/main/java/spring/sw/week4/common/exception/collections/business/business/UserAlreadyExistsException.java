package spring.sw.week4.common.exception.collections.business.business;

import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.business.AuthExceptionMessage.UserAlreadyExists;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super(UserAlreadyExists);
    }
}