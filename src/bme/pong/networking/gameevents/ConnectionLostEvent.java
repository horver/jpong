package bme.pong.networking.gameevents;

public class ConnectionLostEvent implements IGameEvent {
    // Nada

    public String serialize() { return ""; }
    public void deserialize(String sObj) {}
}
