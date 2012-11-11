package checkers.model;

/**
 *
 * @author Kapellan
 */
public class NoSuchDirectionException extends Exception {

    NoSuchDirectionException(String message) {
        super(message);
    }

    NoSuchDirectionException() {
        super();
    }
}
