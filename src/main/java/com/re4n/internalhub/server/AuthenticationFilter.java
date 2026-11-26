package com.re4n.internalhub.server;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.service.AuthenticationService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AuthenticationFilter extends Filter {
    private final AuthenticationService authenticationService;
    private final UserDAO userDAO;
    private final JsonResponder jsonResponder;

    public AuthenticationFilter(AuthenticationService authenticationService, UserDAO userDAO,  JsonResponder jsonResponder) {
        this.authenticationService = authenticationService;
        this.userDAO = userDAO;
        this.jsonResponder = jsonResponder;
    }


    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            jsonResponder.sendEmpty(exchange, 204);
            return;
        }

        String authenticationHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if(authenticationHeader == null ||!authenticationHeader.startsWith("Bearer ")) {
            jsonResponder.sendError(exchange, new AppException(AppError.UNAUTHENTICATED));
            return;
        }
        String token = authenticationHeader.substring(7);
        User user;
        try {
            Long userId = authenticationService.userIdExtractor(token);
            user = userDAO.findById(userId);
        }catch (AppException e) {
            jsonResponder.sendError(exchange, e);
            return;
        }catch (Exception e) {
            AppException appException = new AppException(AppError.INTERNAL_ERROR, e);
            System.err.println("Incident " + appException.getIncidentId());
            e.printStackTrace();
            jsonResponder.sendError(exchange, appException);
            return;
        }
        if(user == null || !Boolean.TRUE.equals(user.getActive())){
            jsonResponder.sendError(exchange, new AppException(AppError.UNAUTHENTICATED));
            return;
        }
        exchange.setAttribute("authenticatedUser", user);
        chain.doFilter(exchange);

    }

    @Override
    public String description() {
        return "JWT token validation filter and user context injection";
    }
}

