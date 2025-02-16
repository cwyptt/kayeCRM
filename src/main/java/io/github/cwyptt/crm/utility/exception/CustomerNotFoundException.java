package io.github.cwyptt.crm.utility.exception;

public class CustomerNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Customer not found";

    public CustomerNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CustomerNotFoundException(Long id) {
        super("Customer with ID " + id + " not found");
    }

    public CustomerNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
