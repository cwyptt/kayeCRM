package io.github.cwyptt.crm.utility.exception;

public class DtoConvertionFailedException extends RuntimeException {
    public DtoConvertionFailedException(String source) {
        super(source + " Dto conversion failed. Check logs for more information.");
    }

    public DtoConvertionFailedException() {
    }
}
