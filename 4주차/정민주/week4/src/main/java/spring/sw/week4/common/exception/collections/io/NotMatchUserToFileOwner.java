package spring.sw.week4.common.exception.collections.io;

import java.io.IOException;

import static spring.sw.week4.common.exception.message.io.IoExceptionMessage.NotMatchUserToFileOwner;

public class NotMatchUserToFileOwner extends IOException {
    public NotMatchUserToFileOwner(){
        super(NotMatchUserToFileOwner);
    }

}
