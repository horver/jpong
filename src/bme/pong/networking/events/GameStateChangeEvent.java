package bme.pong.networking.events;

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

    public String getName() {
        return "Game state change to: " + newState.name();
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(newState);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        newState = (GameState) inputStream.readObject();
    }
}
