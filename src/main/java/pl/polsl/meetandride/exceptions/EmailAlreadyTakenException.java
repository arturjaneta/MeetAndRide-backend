package pl.polsl.meetandride.exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String msg) { super(msg); }
}
