package com.re4n.internalhub.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.LoginRequest;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.server.JsonResponder;
import com.re4n.internalhub.service.AuthenticationService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class AuthHandler implements HttpHandler {
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;
    private final JsonResponder jsonResponder;

    public AuthHandler(AuthenticationService authenticationService, ObjectMapper objectMapper, JsonResponder jsonResponder) {
        this.authenticationService = authenticationService;
        this.objectMapper = objectMapper;
        this.jsonResponder = jsonResponder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            jsonResponder.sendEmpty(exchange, 204);
            return;
        }

        if ("/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleLogin(exchange);
        } else {
            jsonResponder.sendError(exchange, new AppException(AppError.RESOURCE_NOT_FOUND));
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody()) {
            LoginRequest request = objectMapper.readValue(requestBody, LoginRequest.class);

            if (request == null
                    || request.email() == null || request.email().isBlank()
                    || request.password() == null || request.password().isBlank()) {
                jsonResponder.sendError(exchange, new AppException(AppError.VALIDATION_FAILED));
                return;
            }

            AuthUserResult result = authenticationService.login(request.email(), request.password());
            jsonResponder.send(exchange, 200, result);
        } catch (AppException e) {
            jsonResponder.sendError(exchange, e);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            jsonResponder.sendError(exchange, new AppException(AppError.VALIDATION_FAILED, e));
        } catch (Exception e) {
            AppException ex = new AppException(AppError.INTERNAL_ERROR, e);
            System.err.println("Incident " + ex.getIncidentId());
            e.printStackTrace();
            jsonResponder.sendError(exchange, ex);
        }
    }
}

