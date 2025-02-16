package io.github.cwyptt.crm.utility.exception;

public class EmailAlreadyExistsException extends IllegalArgumentException {

    private static final String DEFAULT_MESSAGE = "Email already exists";

    public EmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyExistsException(String s) {
        super(s);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public EmailAlreadyExistsException(Long id) {
        super("Email already exists for Customer ID: " + id);
    }
}
