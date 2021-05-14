package bme.pong.networking.gameevents;

public class PlayerKeyUpEvent implements IGameEvent {
    public int keyCode;

    public String serialize() { return ""; }
    public void deserialize(String sObj) {}
}
