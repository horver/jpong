package bme.pong.networking.gameevents;

import bme.pong.utils.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GameStateChangeEvent implements IGameEvent, Serializable {
    public GameState newState;

    public void writeObject(ObjectOutputStream oos) throws IOException, ClassNotFoundException {
        oos.writeObject(newState);
    }
    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        newState = (GameState) ois.readObject();
    }
}
