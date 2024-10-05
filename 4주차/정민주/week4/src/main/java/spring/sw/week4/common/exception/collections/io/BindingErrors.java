package spring.sw.week4.common.exception.collections.io;

import static spring.sw.week4.common.exception.message.io.IoExceptionMessage.BindingErrorMessage;

public class BindingErrors extends RuntimeException {
    public BindingErrors() {
        super(BindingErrorMessage);
    }
}
