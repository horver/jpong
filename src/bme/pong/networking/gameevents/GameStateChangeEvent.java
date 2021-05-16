package bme.pong.networking.gameevents;

import bme.pong.entities.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameStateChangeEvent implements IGameEvent, Serializable {
    public GameState newState;

    public GameStateChangeEvent(GameState gs) {
        newState = gs;
    }

    public String getName() { return "Game state change to: " + newState.name(); }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(newState);
    }
    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        newState = (GameState) ois.readObject();
    }
}
