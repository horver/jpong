package bme.pong.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;
import java.net.*;

import bme.pong.networking.gameevents.*;

public class NetworkListener implements Runnable {

    private Socket _sock;
    private EventBus _bus;
    private Logger _logger;
    private ObjectInputStream _ois;

    public NetworkListener(Socket sock, EventBus bus) throws IOException {
        this._sock = sock;
        this._bus = bus;
        this._logger = Logger.getLogger(this.getClass().getName());
        this._ois = new ObjectInputStream(_sock.getInputStream());

    }

    public void detach() {
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        _logger.info("Starting listener thread - thread ID is: " + Thread.currentThread().getId());
        while (true) {
            try {
                enqueueEvent((IGameEvent) _ois.readObject());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void enqueueEvent(IGameEvent event) {
        if (event instanceof ConnectionRequestEvent) {
            // Guest to host only, hence the pushing to a different queue.
            // Also, this event has to be received by both parties
            _bus.pushOutgoing(event);
        }
        else {
            _bus.pushIncoming(event);
        }
    }
}
