package com.re4n.internalhub.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dto.AuthUserResult;
import com.re4n.internalhub.dto.RoleResult;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DashboardHandler implements HttpHandler {
    private final ObjectMapper objectMapper;
    private final RoleDAO roleDAO;

    public DashboardHandler(ObjectMapper objectMapper, RoleDAO roleDAO) {
        this.objectMapper = objectMapper;
        this.roleDAO = roleDAO;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if(!"GET".equalsIgnoreCase(exchange.getRequestMethod())){
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        User user = (User) exchange.getAttribute("authenticatedUser");
        Role role = roleDAO.findById(user.getRoleId());
        RoleResult roleResult = (role != null) ? new RoleResult(role.getRoleName(), role.getSeniorityLevel().name(), role.getRoleType().name()) : null;
        AuthUserResult responseData = new AuthUserResult(
                null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmployeeId(),
                user.getHireDate(),
                user.getDepartment(),
                user.getCorporateEmail(),
                roleResult
        );

        byte[] body = objectMapper.writeValueAsBytes(responseData);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, body.length);

        try(OutputStream os = exchange.getResponseBody()){
            os.write(body);
        }
    }
}
