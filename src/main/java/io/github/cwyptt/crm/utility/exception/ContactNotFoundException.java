package io.github.cwyptt.crm.utility.exception;

public class ContactNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Customer not found";

    public ContactNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ContactNotFoundException(Long id) {
        super("Customer with ID " + id + " not found");
    }
}
