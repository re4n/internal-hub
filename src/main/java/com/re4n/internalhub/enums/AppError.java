package com.re4n.internalhub.enums;

public enum AppError {
    DB_PERSISTENCE_VIOLATION("IHSC-E100", "Operation aborted for security reasons."),
    DB_CONNECTION_FAILED("IHSC-E101", "Communication failure with the server."),

    RESOURCE_NOT_FOUND("IHBC-E200", "The parameter being searched doesn't exist."),
    DUPLICATE_IDENTITY("IHBC-E201", "The email already exists."),
    VALIDATION_FAILED("IHBC-E202", "The access level is outside the permitted range.");


    private final String code;
    private final String message;

    AppError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}