package com.re4n.internalhub.dto;

public record ErrorResponse(
        String code,
        String message,
        String incidentId
) {}
