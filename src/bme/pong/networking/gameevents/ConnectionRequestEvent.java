package bme.pong.networking.gameevents;

public class ConnectionRequestEvent implements IGameEvent {
    public String guestName;

    public ConnectionRequestEvent(String name) {
        guestName = name;
    }
    public String serialize() { return ""; }
    public void deserialize(String sObj) {}
}
