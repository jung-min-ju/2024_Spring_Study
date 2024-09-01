package spring.sw.week4.common.exception.collections.io;

import spring.sw.week4.common.exception.message.io.S3ExceptionMessage;

import java.io.IOException;

public class S3AccessException extends IOException {

    public S3AccessException() {
        super(S3ExceptionMessage.S3AccessException);
    }

}

