package pl.polsl.meetandride.exceptions;

public class NoPermissionsException extends RuntimeException {
    public NoPermissionsException(String msg) {
        super(msg);
    }
}
