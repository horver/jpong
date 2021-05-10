package bme.pong.networking.gameevents;

public interface IGameEvent {
    String serialize();
    void deserialize(String sObj);
}
