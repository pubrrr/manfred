package manfred.game.exception;

public class ManfredException extends Exception {
    public ManfredException(String message) {
        super(message);
    }

    public ManfredException(Throwable cause) {
        super(cause);
    }

    public ManfredException(String message, Throwable cause) {
        super(message, cause);
    }
}
