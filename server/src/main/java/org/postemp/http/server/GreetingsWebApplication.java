package org.postemp.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GreetingsWebApplication implements MyWebApplication {

    private String name;

    public String getName() {
        return name;
    }

    public GreetingsWebApplication() {
        this.name = "Greetings Web Application";
    }

    @Override
    public void execute(Request request, OutputStream output) throws IOException {
        String result = "";
        String username = request.getParam("username");
        output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" + getName() + "</h1><h2>Hello, " + username + "</body></html>").getBytes(StandardCharsets.UTF_8));
    }
}
