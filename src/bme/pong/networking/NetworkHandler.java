package bme.pong.networking;

import bme.pong.utils.IConfigMgr;

import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.*;

public class NetworkHandler implements Runnable {
    private int _port;
    private EventBus _bus;
    private Logger _logger;

    private Socket _socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public NetworkHandler(EventBus bus, IConfigMgr cfg) {
        this._bus = bus;
        this._port = Integer.parseInt(cfg.getKey("port"));
        this._logger = Logger.getLogger(this.getClass().getName());
    }

    public void setHost() {
        // listen() and accept()
    }

    public void setGuest() {
        // connect()
    }

    public void run() {
        this._logger.info("Starting networking thread");
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
