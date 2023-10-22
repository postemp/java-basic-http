package org.postemp.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerClientThread extends Thread {
    private static final Logger logger = LogManager.getLogger(ServerClientThread.class.getName());
    Socket socket;
    int clientNo;
    Map<String, MyWebApplication> router = new HashMap<>(); // создаем маршрутизатор аналог сервлета
    ServerClientThread(Socket inSocket, int counter) {
        socket = inSocket;
        clientNo = counter;
        router.put("/calculator", new CalculatorWebApplication());
        router.put("/greetings", new GreetingsWebApplication());
    }

    public void run(){
        try {
            logger.debug("Начинаем обработку данных от клиента под номером " + clientNo);
            byte[] buffer = new byte[2048];
            int n = 0; // смотрим сколько байтов прилетело
            n = socket.getInputStream().read(buffer);
            String rawRequest = new String(buffer, 0, n); // собираем строку из байтов буффера
            Request request = new Request(rawRequest);
            logger.debug("Получен запрос - "+ request.show());

            boolean executed = false;
            for (Map.Entry<String, MyWebApplication> e : router.entrySet()) {
                if (request.getUri().startsWith(e.getKey())) {
                    e.getValue().execute(request, socket.getOutputStream());
                    executed = true;
                    break;
                }
            }
            if (!executed) {
                logger.trace("Этот сырой запрос не смогли обработать: " + rawRequest);
                socket.getOutputStream().write(("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Unknown applicaton</h1></body></html>").getBytes(StandardCharsets.UTF_8));
            }
            socket.close();
        } catch(Exception ex){
            logger.error(ex);
        }finally{
            logger.debug("Обработка данных клиента под номером " + clientNo + "  закончена");
        }
    }
}
