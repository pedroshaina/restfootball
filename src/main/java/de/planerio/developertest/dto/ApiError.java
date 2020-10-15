package de.planerio.developertest.dto;

import java.time.Instant;

public class ApiError {
    private final int statusCode;
    private final String errorMessage;
    private final Instant timestamp;

    public ApiError(final int statusCode, final String errorMessage, final Instant timestamp) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }

    public ApiError(final int statusCode, final String errorMessage) {
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
}
