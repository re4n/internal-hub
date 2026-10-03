package com.re4n.internalhub.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.re4n.internalhub.dao.RoleDAO;
import com.re4n.internalhub.dao.UserDAO;
import com.re4n.internalhub.server.handler.AuthHandler;
import com.re4n.internalhub.service.AuthenticationService;
import com.re4n.internalhub.service.AuthenticationServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpAppServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        UserDAO userDAO = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userDAO, roleDAO);

        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/auth/login", new AuthHandler(authenticationService, objectMapper));
        server.setExecutor(null);
        System.out.println("HTTP server running on: http://localhost:" + PORT);
        server.start();
    }
}
