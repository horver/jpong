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
    private AbortInterface _abortHandler;

    public NetworkListener(Socket sock, EventBus bus, AbortInterface ah) throws IOException {
        this._sock = sock;
        this._bus = bus;
        this._logger = Logger.getLogger(this.getClass().getName());
        this._ois = new ObjectInputStream(_sock.getInputStream());
        this._abortHandler = ah;
    }

    public void detach() {
        (new Thread(this, "Socket listener")).start();
    }

    @Override
    public void run() {
        _logger.info("Starting listener thread - thread ID is: " + Thread.currentThread().getId());
        while (true) {
            try {
                _abortHandler.check();
                _logger.info("Waiting to read event");
                enqueueEvent((IGameEvent) _ois.readObject());
            }
            catch (AbortException ex) {
                _logger.info("Thread " + Thread.currentThread().getName() + " has been aborted");
                break;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                _logger.info("NetworkHandler shat itself, exiting");
                System.exit(1);
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
