package bme.pong.networking;

import bme.pong.storages.PropertyStorage;
import bme.pong.networking.gameevents.*;

import java.io.IOException;
import java.util.logging.Logger;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.*;

public class NetworkHandler implements Runnable {
    public enum NetworkRole {
        HOST,
        GUEST
    }

    private int _port;
    private String _host;
    private String _playerName;  // for ConnectionEstablishedEvent
    private EventBus _bus;
    private Logger _logger;

    private ServerSocket _serverSock;
    private Socket _sock;
    private ObjectOutputStream _oos;
    private NetworkRole _role;

    public NetworkHandler(EventBus bus, PropertyStorage storage) {
        this._bus = bus;
        this._port = storage.getHostPort();
        this._host = storage.getHostAddress();
        this._playerName = storage.getPlayerName();
        this._logger = Logger.getLogger(this.getClass().getName());
    }

    public void detach() {
        (new Thread(this)).start();
    }

    public void setNetworkRole(NetworkRole role) {
        _role = role;
    }

    @Override
    public void run() {
        try {
            this._logger.info("Starting networking thread - thread ID is: " + Thread.currentThread().getId());
            if (NetworkRole.HOST == _role) {
                setHost();
            } else {
                setGuest();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setHost() throws IOException {
        _serverSock = new ServerSocket(_port);
        _sock = _serverSock.accept();
        initStreams();
    }

    private void setGuest() throws IOException {
        _sock = new Socket(_host, _port);
        initStreams();
        ConnectionRequestEvent connReq = new ConnectionRequestEvent(_playerName);
        _logger.info("Sending connection request event");
        sendEvent((IGameEvent) connReq);
    }

    private void initStreams() {
        try {
            _oos = new ObjectOutputStream(_sock.getOutputStream());
            NetworkListener listener = new NetworkListener(_sock, _bus);
            listener.detach();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void monitorEventBus() {
        while (true) {
            try {
                IGameEvent event = _bus.popOutgoingBlocking();
                _logger.info("Popped event from bus: " + event.getClass().getName());
                sendEvent(event);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendEvent(IGameEvent event) {
        try {
            _logger.info("Sending class: " + event.getClass().getName());
            _oos.writeObject(event);
        }
        catch (Exception ex) {
            _logger.info("Error while sending event");
            ex.printStackTrace();
        }
    }

    private void handleConnectionRequest(ConnectionRequestEvent req) {
        ConnectionEstablishedEvent respEvent = new ConnectionEstablishedEvent(req.guestName, this._playerName);
        sendEvent((IGameEvent) respEvent);
        _bus.pushIncoming((IGameEvent) respEvent);
    }
}
