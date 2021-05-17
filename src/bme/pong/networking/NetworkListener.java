package bme.pong.networking;

import bme.pong.networking.events.ConnectionRequestEvent;
import bme.pong.networking.events.IGameEvent;
import bme.pong.threading.ThreadMgr;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkListener implements Runnable {

    private Socket _sock;
    private EventBus _bus;
    private Logger _logger;
    private ObjectInputStream _ois;
    private ThreadMgr _threadManager;

    public NetworkListener(Socket sock, EventBus bus, ThreadMgr tmgr) throws IOException {
        this._sock = sock;
        this._bus = bus;
        this._logger = Logger.getLogger(this.getClass().getName());
        this._ois = new ObjectInputStream(_sock.getInputStream());
        this._threadManager = tmgr;
    }

    public void detach() {
        _threadManager.startThread(this, "Socket listener");
    }

    @Override
    public void run() {
        _logger.info("Starting listener thread - thread ID is: " + Thread.currentThread().getId());
        while (true) {
            try {
                _logger.info("Waiting to read event");
                enqueueEvent((IGameEvent) _ois.readObject()); // readObject() doesn't throw InterruptedException
            }
            catch (Exception ex) {
                _logger.info("Shutting down NetworkListener thread");
                break;
            }
        }
    }

    private void enqueueEvent(IGameEvent event) {
        _logger.info("Received event with name: " + event.getName());
        if (event instanceof ConnectionRequestEvent) {
            // Guest to host only, hence the pushing to a different queue.
            // Also, this event has to be sent to both parties
            _bus.pushOutgoing(event);
        }
        else {
            _bus.pushIncoming(event);
        }
    }
}
