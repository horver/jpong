package bme.pong.storages;

import java.util.logging.Logger;

import bme.pong.utils.IConfigMgr;
import bme.pong.utils.IniParser;

public class PropertyStorage {
    private String playerName;
    private String hostAddress;
    private int hostPort;
    private boolean isClient;
    private IConfigMgr configParser;
    private final Logger logger;

    public PropertyStorage(String configPath) {
        this.logger = Logger.getLogger(this.getClass().getName());
        try {
            this.configParser = IniParser.fromFile(configPath);
            load();
        }
        catch(Exception ex) {
            logger.info("Error loading settings from file, loading defaults");
            this.configParser = new IniParser();
            loadDefaults();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public void save(String configPath) {
        this.configParser.setKey("player_name", playerName);
        this.configParser.setKey("host_address", hostAddress);
        this.configParser.setKey("port", Integer.toString(hostPort));
        this.configParser.saveToFile(configPath);
    }

    private void loadDefaults() {
        playerName = "Player1";
        hostAddress = "127.0.0.1";
        hostPort = 12345;
        isClient = true;
    }

    public void load() {
        playerName = configParser.getKey("player_name", "Player1");
        hostAddress = configParser.getKey("host_address", "127.0.0.1");
        hostPort = Integer.parseInt(configParser.getKey("port", "12345"));
        isClient = true;
    }
}
