package com.re4n.internalhub.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.re4n.internalhub.dto.ErrorResponse;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class JsonResponder {
    private final ObjectMapper objectMapper;

    public JsonResponder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void send(HttpExchange exchange, int status, Object body) throws IOException {
        byte[] bytes = objectMapper.writeValueAsBytes(body);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public void sendError(HttpExchange exchange, AppException exception) throws IOException {
        AppError error = exception.getErrorType();
        ErrorResponse responseBody = new ErrorResponse(error.getCode(), error.getMessage(), exception.getIncidentId());
        send(exchange, error.getHttpStatus(), responseBody);
    }

    public void sendEmpty(HttpExchange exchange, int status) throws IOException {
        exchange.sendResponseHeaders(status, -1);
    }
}
