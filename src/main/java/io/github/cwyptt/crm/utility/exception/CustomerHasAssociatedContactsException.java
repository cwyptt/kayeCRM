package io.github.cwyptt.crm.utility.exception;

public class CustomerHasAssociatedContactsException extends RuntimeException {
    public CustomerHasAssociatedContactsException(Long customerId) {
      super("Cannot delete customer with ID " + customerId + " because they have associated contacts");
    }
}
