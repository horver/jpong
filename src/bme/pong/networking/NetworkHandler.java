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
    private final boolean isClient = Main.propertyStorage.isClient();
    private EventBus _bus;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private ServerSocket listener;
    private Socket clientSocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public NetworkHandler(EventBus bus) {
        this._bus = bus;
    }

    public void setHost() {
        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            logger.info("Error set server socket");
            e.printStackTrace();
        }
    }

    public void setGuest() {
        String serverAddress = Main.propertyStorage.getHostAddress();
        try {
            clientSocket = new Socket(serverAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.logger.info("Starting networking thread");
        try {
            while (true) {
                Socket socket = listener.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void monitorEventBus() {

    }

    private void pollSocket() {

    }

    private void sendEvent() {

    }

    private void recvEvent() {

    }
}
