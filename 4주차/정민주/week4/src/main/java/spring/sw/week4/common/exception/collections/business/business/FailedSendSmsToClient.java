package spring.sw.week4.common.exception.collections.business.business;


import spring.sw.week4.common.exception.collections.business.BusinessException;
import spring.sw.week4.common.exception.message.business.SmsExceptionMessage;

public class FailedSendSmsToClient extends BusinessException {
    public FailedSendSmsToClient() {
        super(SmsExceptionMessage.FailedSendSmsToClient);
    }
}
