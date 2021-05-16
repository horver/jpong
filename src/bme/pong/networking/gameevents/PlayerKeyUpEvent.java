package bme.pong.networking.gameevents;

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

    public String getName() { return "Player key up event with keycode: " + action.name(); }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(action);
    }
    public void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        action = (MoveAction) ois.readObject();
    }
}
