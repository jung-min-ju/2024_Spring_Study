package spring.study.week3.common.exception.collections;

public class InvalidRequestException extends RuntimeException {
    protected InvalidRequestException(String message) {
        super(message);
    }

}