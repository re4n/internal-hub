package com.re4n.internalhub.server.handler;

import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dto.ProfileResult;
import com.re4n.internalhub.dto.RoleResult;
import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;
import com.re4n.internalhub.model.Role;
import com.re4n.internalhub.model.User;
import com.re4n.internalhub.server.JsonResponder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ProfileHandler implements HttpHandler {
    private final RoleDAO roleDAO;
    private final JsonResponder jsonResponder;

    public ProfileHandler(RoleDAO roleDAO,  JsonResponder jsonResponder) {
        this.roleDAO = roleDAO;
        this.jsonResponder = jsonResponder;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            jsonResponder.sendEmpty(exchange, 204);
            return;
        }

        if(!"/profile".equals(exchange.getRequestURI().getPath())) {
            jsonResponder.sendError(exchange, new AppException(AppError.RESOURCE_NOT_FOUND));
            return;
        }

        if(!"GET".equalsIgnoreCase(exchange.getRequestMethod())){
            jsonResponder.sendEmpty(exchange, 405);
            return;
        }

        User user = (User) exchange.getAttribute("authenticatedUser");
        if(user == null){
            jsonResponder.sendError(exchange, new AppException(AppError.UNAUTHENTICATED));
            return;
        }
        try{
            Role role = user.getRoleId() != null ? roleDAO.findById(user.getRoleId()) : null;
            RoleResult roleResult = (role != null)
                    ? new RoleResult(role.getRoleName(), role.getSeniorityLevel().name(),
                    role.getRoleType().name()) : null;
            ProfileResult responseData = new ProfileResult(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmployeeId(),
                    user.getHireDate(),
                    user.getDepartment(),
                    user.getCorporateEmail(),
                    roleResult
            );
            jsonResponder.send(exchange, 200, responseData);
        }catch (AppException e){
            jsonResponder.sendError(exchange, e);
        }catch (Exception e){
            AppException appException = new AppException(AppError.INTERNAL_ERROR, e);
            System.err.println("Incident " + appException.getIncidentId());
            e.printStackTrace();
            jsonResponder.sendError(exchange, appException);
        }
    }
}
