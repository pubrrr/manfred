package game.exception;

abstract public class ManfredException extends Exception {
    protected final static String type = "ManfredException";

    public ManfredException(String type, String message) {
        super(type + ": " + message);
    }
}
