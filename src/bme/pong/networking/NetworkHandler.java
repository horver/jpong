package bme.pong.networking;

import bme.pong.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkHandler implements Runnable {
    private final int port = Main.propertyStorage.getHostPort();
    private boolean isClient = false;
    private String serverAddress;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private ServerSocket serverSocket;

    public MessageBus getMessageBus() {
        return messageBus;
    }

    private final MessageBus messageBus = new MessageBus();

    public void setHost() {
        logger.info("Host mode");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient() {
        isClient = true;
        serverAddress = Main.propertyStorage.getHostAddress();
        logger.info("Client mode");
    }

    public void run() {
        if (isClient) {
            try {
                Socket socket = new Socket(serverAddress, port);
                exchangeData(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Socket socket = serverSocket.accept();
                exchangeData(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exchangeData(Socket socket) {
        try {
            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            NetworkMessage message;
            while (socket.isConnected()) {
                synchronized (messageBus.out) {
                    message = messageBus.out;
                }

                try {
                    writer.writeObject(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    message = (NetworkMessage) reader.readObject();
                    if (message != null) {
                        synchronized (messageBus.in) {
                            messageBus.in = message;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
