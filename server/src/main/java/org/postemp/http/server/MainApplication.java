package org.postemp.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainApplication {
    public static final int PORT = 8189;
    private static final Logger logger = LogManager.getLogger(MainApplication.class.getName());
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Сервер запущен, порт: " + PORT);
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            int clientCounter = 0;
            while (true) {
                logger.debug("Ждем нового соединения");
                clientCounter++;
                try {
                    Socket socket = serverSocket.accept();
                    logger.debug("Клиент подключился: " + socket.isConnected());
                    executorService.execute(new ServerClientThread(socket, clientCounter));
                } catch (Exception e) {
                    logger.error("Исключение: "+e);
                }
                logger.debug("Запустили thread, переход на следующий цикл");
            }
        } catch (IOException e) {
            logger.error("Исключение: "+e);
        }
    }
}
