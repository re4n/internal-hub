package com.re4n.internalhub.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.ErrorResponse;
import com.re4n.internalhub.dto.LoginRequest;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.service.AuthenticationService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AuthHandler implements HttpHandler {
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    public AuthHandler(AuthenticationService authenticationService, ObjectMapper objectMapper) {
        this.authenticationService = authenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleLogin(exchange);
        }else {
            sendAppErrorResponse(exchange, new AppException(AppError.RESOURCE_NOT_FOUND));
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException{
        try(InputStream requestBody = exchange.getRequestBody()) {
            LoginRequest request = objectMapper.readValue(requestBody, LoginRequest.class);

            if(request == null
                    || request.email() == null || request.email().isBlank()
                    || request.password() == null || request.password().isBlank()){
                sendAppErrorResponse(exchange, new AppException(AppError.VALIDATION_FAILED));
                return;
            }

            AuthUserResult result = authenticationService.login(request.email(), request.password());
            sendJsonResponse(exchange, 200, result);
        }catch (AppException e){
           sendAppErrorResponse(exchange, e);
        }catch (com.fasterxml.jackson.core.JsonProcessingException e){
            sendAppErrorResponse(exchange, new AppException(AppError.VALIDATION_FAILED, e));
        } catch (Exception e){
            sendAppErrorResponse(exchange, new AppException(AppError.VALIDATION_FAILED));
        }
    }

    private void sendAppErrorResponse(HttpExchange exchange, AppException exception) throws IOException {
        AppError error = exception.getErrorType();
        ErrorResponse responseBody = new ErrorResponse(
                error.getCode(), 
                error.getMessage(), 
                exception.getIncidentId()
        );
        sendJsonResponse(exchange, error.getHttpStatus(), responseBody);
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object body) throws IOException {
        byte[] bytes = objectMapper.writeValueAsBytes(body);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try(OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(bytes);
        }
    }
}

