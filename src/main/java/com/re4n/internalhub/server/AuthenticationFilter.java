package com.re4n.internalhub.server;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.AuthenticationService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class AuthenticationFilter extends Filter {
    private final AuthenticationService authenticationService;
    private final UserDAO userDAO;

    public AuthenticationFilter(AuthenticationService authenticationService, UserDAO userDAO) {
        this.authenticationService = authenticationService;
        this.userDAO = userDAO;
    }


    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String authenticationHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if(authenticationHeader == null ||!authenticationHeader.startsWith("Bearer ")) {
            sendUnauthorized(exchange, "Missed Authorization header");
            return;
        }
        String token = authenticationHeader.substring(7);

        try {
            Long userId = authenticationService.userIdExtractor(token);
            User user = userDAO.findById(userId);

            if(user == null || !Boolean.TRUE.equals(user.getActive())){
                sendUnauthorized(exchange, "Unauthorized");
                return;
            }
            exchange.setAttribute("authenticatedUser", user);
            chain.doFilter(exchange);

        }catch(Exception e){
            sendUnauthorized(exchange, "Unauthorized");
        }

    }

    @Override
    public String description() {
        return "JWT token validation filter and user context injection";
    }

    private void sendUnauthorized(HttpExchange exchange, String message) throws IOException{
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");

        String jsonResponse = String.format("{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}", message);
        byte[] response = jsonResponse.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(401, response.length);
        try(OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(response);
        }
    }
}

