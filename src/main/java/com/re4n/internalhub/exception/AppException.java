package com.re4n.internalhub.exception;

import com.re4n.internalhub.enums.AppError;

public class AppException extends RuntimeException {
    private final AppError errorType;
    private final String incidentId;

    public AppException(AppError errorType, Throwable cause){
        super(errorType.getMessage(), cause);
        this.errorType = errorType;

        this.incidentId = java.util.UUID.randomUUID().toString().substring(0,8);
    }

    public AppError getErrorType() {return errorType;}

    public String getIncidentId(){ return incidentId;}

    public String getUserMessage(){
        return "ERROR: " + incidentId + " Contact an Admin";
    }

}
