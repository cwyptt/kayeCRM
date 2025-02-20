package io.github.cwyptt.crm.utility.exception;

public class CompanyNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Company not found";

    public CompanyNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CompanyNotFoundException(Long id) {
        super("Company with ID " + id + " not found");
    }
}
