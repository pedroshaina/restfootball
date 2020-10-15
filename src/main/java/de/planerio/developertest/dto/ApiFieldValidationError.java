package de.planerio.developertest.dto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ApiFieldValidationError {
    private final int statusCode;
    private final String errorMessage;
    private final Instant timestamp;
    private final Map<String, String> invalidFields = new HashMap<>();

    public ApiFieldValidationError(final int statusCode, final String errorMessage, final Instant timestamp) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public ApiFieldValidationError(final int statusCode, final String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.timestamp = Instant.now();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getInvalidFields() {
        return invalidFields;
    }

    public void addInvalidField(final String fieldName, final String errorDescription) {
        this.invalidFields.put(fieldName, errorDescription);
    }

}
