package spring.sw.week4.common.exception.collections.business.business;

import spring.sw.week4.common.exception.collections.business.BusinessException;
import static spring.sw.week4.common.exception.message.business.AuthCodeExceptionMessage.UnVerifiedInfo;

public class UnVerifiedInfoException extends BusinessException {

    public UnVerifiedInfoException() {
        super( UnVerifiedInfo);
    }
}
