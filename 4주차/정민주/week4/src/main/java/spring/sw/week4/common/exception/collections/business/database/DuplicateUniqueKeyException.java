package spring.sw.week4.common.exception.collections.business.database;


import spring.sw.week4.common.exception.collections.business.BusinessException;

import static spring.sw.week4.common.exception.message.database.DatabaseExceptionMesseage.DuplicatedUniqueKey;

public class DuplicateUniqueKeyException extends BusinessException {
    public DuplicateUniqueKeyException() {
        super(DuplicatedUniqueKey);
    }
}
