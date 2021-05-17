package bme.pong.networking.gameevents;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PlayerReadyEvent implements IGameEvent, Serializable {

    public String getName() { return "Game started"; }

    public void writeObject(ObjectOutputStream oos) {}
    public void readObject(ObjectInputStream ois)  {}
}
