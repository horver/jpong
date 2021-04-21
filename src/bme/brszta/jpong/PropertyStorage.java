package bme.brszta.jpong;

import java.io.*;
import java.util.Scanner;

public class PropertyStorage {
    private String playerName;
    private String hostAddress;
    private int hostPort;
    private boolean isClient;
    private static final String configPath = "game.conf";

    public PropertyStorage() {
        load();
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

    public void save() {
        try (PrintWriter writer = new PrintWriter(configPath)) {
            writer.println(playerName);
            writer.println(hostAddress);
            writer.println(hostPort);
            writer.println(isClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaults() {
        playerName = "Player1";
        hostAddress = "127.0.0.1";
        hostPort = 12345;
        isClient = true;
    }

    public void load() {
        File config = new File(configPath);
        if (config.exists()) {
            int currentLine = 0;
            try (Scanner scanner = new Scanner(new FileInputStream(config))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    switch (currentLine) {
                        case 0:
                            playerName = line;
                            break;
                        case 1:
                            hostAddress = line;
                            break;
                        case 2:
                            hostPort = Integer.parseInt(line);
                            break;
                        case 3:
                            isClient = Boolean.parseBoolean(line);
                            break;
                    }
                    currentLine++;
                }
            } catch (FileNotFoundException e) {
                loadDefaults();
            }
        } else {
            loadDefaults();
        }
    }
}
