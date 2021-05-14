package bme.pong.networking;

import bme.pong.Main;

import javax.net.ServerSocketFactory;
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
            serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
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
            try (
                    Socket socket = new Socket(serverAddress, port);
                    ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream reader = new ObjectInputStream(socket.getInputStream())
            ) {
                NetworkMessage message;
                while (true) {
                    message = (NetworkMessage) reader.readObject();
                    messageBus.pushIncoming(message);

                    message = messageBus.popOutgoing();
                    writer.writeObject(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try (
                    Socket socket = serverSocket.accept();
                    ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream())
            ) {
                NetworkMessage message;
                while (true) {

                    message = messageBus.popOutgoing();
                    writer.writeObject(message);

                    message = (NetworkMessage) reader.readObject();
                    messageBus.pushIncoming(message);

                    // TODO: nemmegybazdmeg
                    if (!socket.isConnected()) {
                        break;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
