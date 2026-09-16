package com.re4n.internalhub.enums;

public enum AppError {
    DB_PERSISTENCE_VIOLATION("IHSC-E100", "Operation aborted for security reasons."),
    DB_CONNECTION_FAILED("IHSC-E101", "Communication failure with the server."),
    DB_DRIVER_NOT_FOUND("IHSC-E102", "Driver MySQL not found."),

    RESOURCE_NOT_FOUND("IHBC-E200", "The parameter being searched doesn't exist."),
    DUPLICATE_IDENTITY("IHBC-E201", "A unique field already exists"),
    VALIDATION_FAILED("IHBC-E202", "Invalid or incomplete input data"),
    FOREIGN_KEY_VIOLATION("IHBC-E203","The resource cannot be removed because it is in use."),

    INVALID_CREDENTIALS("IHAC-E300", "Incorrect username or password."),
    AUTHORIZATION_DENIED("IHAC-E301", "Insufficient permissions to access the requested resource.");


    private final String code;
    private final String message;

    AppError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}