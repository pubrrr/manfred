package manfred.game.exception;

public class InvalidInputException extends ManfredException {
    private static final String type = "InvalidInputException";

    public InvalidInputException(String message) {
        super(type, message);
    }
}
