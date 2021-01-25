package manfred.data;

public class InvalidInputException extends Exception {
    public InvalidInputException(String message, Exception cause) {
        super(message, cause);
    }

    public InvalidInputException(Exception cause) {
        super(cause);
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
