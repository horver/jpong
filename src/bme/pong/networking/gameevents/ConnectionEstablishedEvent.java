package bme.pong.networking.gameevents;

public class ConnectionEstablishedEvent implements IGameEvent {
    public String guestName;
    public String hostName;

    public ConnectionEstablishedEvent(String guestPlayerName, String hostPlayerName) {
        guestName = guestPlayerName;
        hostName = hostPlayerName;
    }

    public String serialize() { return ""; }
    public void deserialize(String sObj) {}
}
