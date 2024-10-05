package spring.sw.week4.common.exception.collections.NotFound;

public class ResourceNotFoundException  extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
