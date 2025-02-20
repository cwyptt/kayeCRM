package io.github.cwyptt.crm.utility.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public String handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("NOT_FOUND", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CompanyHasAssociatedContactsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerHasContacts(CompanyHasAssociatedContactsException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("HAS_CONTACTS", ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    record ErrorResponse(String code, String message) {}
}
