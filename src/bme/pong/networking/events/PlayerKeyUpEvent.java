package bme.pong.networking.events;

import bme.pong.entities.MoveAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerKeyUpEvent implements IGameEvent, Serializable {
    public MoveAction action;

    public PlayerKeyUpEvent(MoveAction act) {
        action = act;
    }

    public String getName() {
        return "Player key up event with keycode: " + action.name();
    }

    @Override
    public void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeObject(action);
    }

    @Override
    public void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        action = (MoveAction) inputStream.readObject();
    }
}
