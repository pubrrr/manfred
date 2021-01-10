package manfred.data;

import java.net.MalformedURLException;

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
