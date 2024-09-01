package spring.sw.week4.common.exception.collections.NotFound;


import spring.sw.week4.common.exception.message.business.AuthExceptionMessage;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(AuthExceptionMessage.UserNotFoundException);
    }
}
