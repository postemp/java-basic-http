package org.postemp.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication {
    public static final int PORT = 8189;
    private static final Logger logger = LogManager.getLogger(MainApplication.class.getName());
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Map<String, MyWebApplication> router = new HashMap<>();
            router.put("/calculator", new CalculatorWebApplication());
            router.put("/greetings", new GreetingsWebApplication());

            logger.info("Сервер запущен, порт: " + PORT);
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            int clientCounter = 0;
            while (true) {
                logger.debug("Ждем нового соединения");
                clientCounter++;
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    logger.debug("Клиент подключился: " + socket.isConnected());
                    byte[] buffer = new byte[2048];
                    int n = socket.getInputStream().read(buffer);
                    String rawRequest = new String(buffer, 0, n);
                    Request request = new Request(rawRequest);
                    logger.debug("Получен запрос - " + request.show());

                    boolean executed = false;
                    for (Map.Entry<String, MyWebApplication> e : router.entrySet()) {
                        if (request.getUri().startsWith(e.getKey())) {
                            executorService.execute(new ServerClientThread(socket, clientCounter, e.getValue(), request));
                            executed = true;
                            break;
                        }
                    }
                    if (!executed) {
                        socket.getOutputStream().write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Unknown application</h1></body></html>").getBytes(StandardCharsets.UTF_8));
                    }
                } catch (Exception e) {
                    logger.error("Исключение: " + e);
                } finally {
                    logger.debug("Запустили , переход на следующий цикл");

                }

            }
        } catch (IOException e) {
            logger.error("Исключение: "+e);
        }
    }
}
