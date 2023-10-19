package org.postemp.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorWebApplication implements MyWebApplication {
    private String name;

    public String getName() {
        return name;
    }

    public CalculatorWebApplication() {
        this.name = "Web Calculator";
    }

    @Override
    public void execute(Request request, OutputStream output) throws IOException {
        String result = "";
        int a = Integer.parseInt(request.getParam("a"));
        int b = Integer.parseInt(request.getParam("b"));
        if (request.getUri().contains("/add")) {
            result = String.format("%d + %d = %d", a, b, a + b);
        } else if (request.getUri().contains("/subtract")) {
            result = String.format("%d - %d = %d", a, b, a - b);
        } else if (request.getUri().contains("/multiply")) {
            result = String.format("%d * %d = %d", a, b, a * b);
        } else if (request.getUri().contains("/divide")) {
            result = String.format("%d / %d = %d", a, b, a / b);
        } else {
            result = "Unknown operation";
        }
        output.write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>" + getName() + "</h1><h2>" + result + "</body></html>").getBytes(StandardCharsets.UTF_8));
    }
}
