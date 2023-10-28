package org.postemp.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GreetingsWebApplication implements MyWebApplication {
    private static final Logger logger = LogManager.getLogger(MainApplication.class.getName());

    private String name;

    public String getName() {
        return name;
    }

    public GreetingsWebApplication() {
        this.name = "Greetings Web Application";
    }

    @Override
    public void execute(Request request, OutputStream output) throws IOException {
        String username = request.getParam("username");
        logger.trace("username=" + username);
        try {
            output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" + getName() + "</h1><h2>Hello, " + username + "</body></html>").getBytes(StandardCharsets.UTF_8));
            logger.trace("output.write");
        } catch (Exception e) {
            logger.trace("Exception=" + e);
        }

    }
}
