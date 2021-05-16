package bme.pong.networking.gameevents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bme.pong.entities.MoveAction;

public class PlayerKeyDownEvent implements IGameEvent, Serializable {
    public MoveAction action;

    public PlayerKeyDownEvent(MoveAction act) {
        action = act;
    }

    public String getName() { return "Player key down event with keycode: " + action.name(); }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(action);
    }
    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        action = (MoveAction) ois.readObject();
    }
}
