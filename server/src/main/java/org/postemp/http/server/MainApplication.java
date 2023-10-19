package org.postemp.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainApplication {
    public static final int PORT = 8189;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Map<String,MyWebApplication> router = new HashMap<>();
            // /calculator
            // /greetings
            router.put("/calculator", new CalculatorWebApplication());
            router.put("/greetings", new GreetingsWebApplication());

            System.out.println("Сервер запущен, порт: " + PORT);
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Клиент подключился");
                byte[] buffer = new byte[2048]; // поставить цикл здесь
                int n = socket.getInputStream().read(buffer); // смотрим сколько байтов прилетело
                String rawRequest = new String(buffer, 0, n); // собираем строку из байтов буффера

                Request request = new Request(rawRequest);
                System.out.println("Получен запрос:");
                request.show();

                boolean executed = false;
                for(Map.Entry<String, MyWebApplication> e : router.entrySet()) {
                    if (request.getUri().startsWith(e.getKey())) {
                        e.getValue().execute(request, socket.getOutputStream());
                        executed = true;
                        break;
                    }
                }
                if (!executed) {
                    socket.getOutputStream().write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Unknown applicaton</h1></body></html>").getBytes(StandardCharsets.UTF_8));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
