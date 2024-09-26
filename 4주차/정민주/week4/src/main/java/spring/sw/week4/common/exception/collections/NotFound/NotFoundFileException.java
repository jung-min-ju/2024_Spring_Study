package spring.sw.week4.common.exception.collections.NotFound;

import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.database.DatabaseExceptionMesseage.NotExitsFile;

public class NotFoundFileException extends ResourceNotFoundException {
    public NotFoundFileException() {
        super( NotExitsFile );
    }
}
