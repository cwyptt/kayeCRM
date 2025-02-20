package io.github.cwyptt.crm.utility.exception;

public class ContactHasCustomerRelationshipException extends RuntimeException {
    public ContactHasCustomerRelationshipException(Long contactId) {
        super("Cannot delete contact with ID " + contactId + " because they are associated with one or more customers");
    }
}
