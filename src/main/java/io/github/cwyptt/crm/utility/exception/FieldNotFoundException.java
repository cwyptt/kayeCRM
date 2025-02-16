package io.github.cwyptt.crm.utility.exception;

public class FieldNotFoundException extends RuntimeException {
    public FieldNotFoundException(String entity, String field) {
        super("The '" + entity + "' entity does not have field: '" + field + "'.");
    }
}

