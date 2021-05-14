package bme.pong.networking.gameevents;

import bme.pong.utils.GameState;

public class GameStateChangeEvent implements IGameEvent {
    public GameState newState;

    public String serialize() { return ""; }
    public void deserialize(String sObj) {}
}
