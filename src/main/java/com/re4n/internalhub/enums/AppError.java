package com.re4n.internalhub.enums;

public enum AppError {
    DB_PERSISTENCE_VIOLATION("IHSC-E100", "Operation aborted for security reasons.", 500),
    DB_CONNECTION_FAILED("IHSC-E101", "Communication failure with the server.", 503),
    DB_DRIVER_NOT_FOUND("IHSC-E102", "Driver MySQL not found.", 500),

    RESOURCE_NOT_FOUND("IHBC-E200", "The parameter being searched doesn't exist.", 404),
    DUPLICATE_IDENTITY("IHBC-E201", "A unique field already exists", 409),
    VALIDATION_FAILED("IHBC-E202", "Invalid or incomplete input data", 400),
    FOREIGN_KEY_VIOLATION("IHBC-E203","The resource cannot be removed because it is in use.", 409),

    INVALID_CREDENTIALS("IHAC-E300", "Incorrect username or password.", 401),
    AUTHORIZATION_DENIED("IHAC-E301", "Insufficient permissions to access the requested resource.", 403);


    private final String code;
    private final String message;
    private final int httpStatus;

    AppError(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus =httpStatus;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public int getHttpStatus() { return httpStatus; }
}