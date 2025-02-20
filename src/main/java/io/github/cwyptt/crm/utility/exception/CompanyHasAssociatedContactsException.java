package io.github.cwyptt.crm.utility.exception;

public class CompanyHasAssociatedContactsException extends RuntimeException {
    public CompanyHasAssociatedContactsException(Long companyId) {
      super("Cannot delete company with ID " + companyId + " because they have associated contacts");
    }
}
