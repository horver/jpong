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
    private AbortInterface _abortHandler;

    public NetworkHandler(EventBus bus, PropertyStorage storage, AbortInterface abortHandler) {
        this._bus = bus;
        this._port = storage.getHostPort();
        this._host = storage.getHostAddress();
        this._playerName = storage.getPlayerName();
        this._logger = Logger.getLogger(this.getClass().getName());
        this._abortHandler = abortHandler;
    }

    public void detach() {
        (new Thread(this, "NetworkHandler")).start();
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
        _logger.info("I'm a host");
        _serverSock = new ServerSocket(_port);
        _sock = _serverSock.accept();
        _oos = new ObjectOutputStream(_sock.getOutputStream());
        initListeners();
    }

    private void setGuest() throws IOException {
        _logger.info("I'm a guest");
        _sock = new Socket(_host, _port);
        _oos = new ObjectOutputStream(_sock.getOutputStream());
        ConnectionRequestEvent connReq = new ConnectionRequestEvent(_playerName);
        _logger.info("Sending connection request event");
        sendEvent((IGameEvent) connReq);
        initListeners();
    }

    private void initListeners() {
        try {
            NetworkListener listener = new NetworkListener(_sock, _bus, _abortHandler);
            listener.detach();
            monitorEventBus();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void monitorEventBus() {
        while (true) {
            try {
                _abortHandler.check();
                IGameEvent event = _bus.popOutgoingBlocking();
                _logger.info("Popped event from bus: " + event.getClass().getName());
                if (event instanceof ConnectionRequestEvent) {
                    handleConnectionRequest((ConnectionRequestEvent) event);
                }
                else {
                    sendEvent(event);
                }
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
