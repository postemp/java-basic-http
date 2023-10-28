package org.postemp.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class ServerClientThread extends Thread {
    private static final Logger logger = LogManager.getLogger(ServerClientThread.class.getName());
    private Socket socket;
    int clientNo;
    private MyWebApplication myWebApplication;
    private Request request;

    ServerClientThread(Socket inSocket, int counter, MyWebApplication myWebApplication, Request request) {
        this.socket = inSocket;
        this.clientNo = counter;
        this.myWebApplication = myWebApplication;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            myWebApplication.execute(request, socket.getOutputStream());

        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            logger.debug("Обработка данных клиента под номером " + clientNo + "  закончена");
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
